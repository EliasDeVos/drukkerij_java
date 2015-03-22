package drukkerij.controller;


import drukkerij.MainApp;
import drukkerij.model.DrukOrder;
import drukkerij.service.DrukOrderService;
import drukkerij.service.DrukOrderServiceImpl;
import drukkerij.service.Listener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class DrukkerijController {
    private MainApp mainApp;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private DrukOrderService drukOrderService = new DrukOrderServiceImpl();
    private ObservableList<DrukOrder> drukOrderList = FXCollections.observableArrayList();
    private ObservableList<DrukOrder> drukOrderFilteredList = FXCollections.observableArrayList();

    //region fxml parameters
    @FXML
    private TableView<DrukOrder> drukOrderTable;
    @FXML
    private Label xPerVelLabel;
    @FXML
    private Label aantalNodigLabel;
    @FXML
    private Label inschietLabel;
    @FXML
    private Label nmkNBLabel;
    @FXML
    private Label qLabel;
    @FXML
    private Label zwLabel;
    @FXML
    private Label zwaar4Z2Label;
    @FXML
    private Label glanzendLabel;
    @FXML
    private Label helderheidLabel;
    @FXML
    private Label soortPapierLabel;
    @FXML
    private Label geplaatstDoorLabel;
    @FXML
    private Label printerLabel;
    @FXML
    private TableColumn<DrukOrder, String> klantDruk;
    @FXML
    private TableColumn<DrukOrder, String> opdrachtDruk;
    @FXML
    private DatePicker drukOrderDatePicker;
    @FXML
    private Label PersoonLabel;
    //endregion


    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        klantDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("klant"));
        opdrachtDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("opdracht"));

        showDrukOrderDetails(null);
        drukOrderTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDrukOrderDetails(newValue));
        drukOrderDatePicker.setValue(LocalDate.now());

        // right click on row menu
        Stack<DrukOrder> deletedLines = new Stack<>();
        drukOrderTable.setUserData(deletedLines);
        MenuItem mnuDel = new MenuItem("Delete row");
        mnuDel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                DrukOrder item = drukOrderFilteredList.get(drukOrderTable.getSelectionModel().getSelectedIndex());
                if (item != null){
                    String fn = item.getKlant();
                   // deletedLines.push(items.remove(item));
                }
            }
        });
        MenuItem mnuUnDel = new MenuItem("Undo Delete");
        mnuUnDel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (!deletedLines.isEmpty())drukOrderFilteredList.add(deletedLines.pop());
            }
        });
        drukOrderTable.setContextMenu(new ContextMenu(mnuDel,mnuUnDel));
        // end right click on row menu
    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        drukOrderTable.setItems(getDrukOrderList(LocalDate.now(), PersoonLabel.getText()));
        // Use Drag and drop to sort tableview
        drukOrderTable.setRowFactory(tv -> {
            TableRow<DrukOrder> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {

                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    DrukOrder draggedPerson = drukOrderTable.getItems().remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = drukOrderTable.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    drukOrderTable.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    drukOrderTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row;
        });
    }


    public DrukkerijController() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/testdb";

            // Create two distinct connections, one for the notifier
            // and another for the listener to show the communication
            // works across connections although this example would
            // work fine with just one connection.
            Connection lConn = DriverManager.getConnection(url, "postgres", "root");
            Connection nConn = DriverManager.getConnection(url, "postgres", "root");

            // Create two threads, one to issue notifications and
            // the other to receive them.
            Listener listener = new Listener(lConn);
            listener.start();
        }catch (Exception e){

        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new drukorder.
     */
    @FXML
    private void handleNewDrukOrder() {
        DrukOrder tempDrukOrder = new DrukOrder();
        boolean okClicked = mainApp.showDrukOrderEditDialog(tempDrukOrder);
        if (okClicked) {
            //add drukorder to db and list
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected drukorder.
     */
    @FXML
    private void handleEditDrukOrder() {
        DrukOrder selectedDrukOrder = drukOrderTable.getSelectionModel().getSelectedItem();
        if (selectedDrukOrder != null) {
            boolean okClicked = mainApp.showDrukOrderEditDialog(selectedDrukOrder);
            if (okClicked) {
                showDrukOrderDetails(selectedDrukOrder);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }

    public void showDrukOrderDetails(DrukOrder drukorder)
    {
        if (drukorder != null)
        {
            xPerVelLabel.setText(drukorder.getxPerVel());
            aantalNodigLabel.setText(drukorder.getAantalNodig());
            inschietLabel.setText(drukorder.getInschiet());
            nmkNBLabel.setText(drukorder.getNmkNB());
            qLabel.setText(drukorder.getQ());
            zwLabel.setText(drukorder.getzW());
            zwaar4Z2Label.setText(drukorder.getZwaar4Z2());
            glanzendLabel.setText(drukorder.getGlanzend());
            helderheidLabel.setText(drukorder.getHelderheid());
            soortPapierLabel.setText(drukorder.getSoortPapier());
            geplaatstDoorLabel.setText(drukorder.getGeplaatstDoor());
            printerLabel.setText(drukorder.getPrinter());
        }
        else
        {
            xPerVelLabel.setText("");
            
        }
    }


    public void addDrukOrder(DrukOrder drukOrder) {
        drukOrderService.addDrukOrder(drukOrder);
    }

    public ObservableList<DrukOrder> getDrukOrderList(LocalDate localDate, String persoon) {
        if (drukOrderList.isEmpty())
        {
            drukOrderList = FXCollections.observableList((List<DrukOrder>) drukOrderService.listDrukOrder());
        }
        drukOrderFilteredList.clear();
        drukOrderFilteredList.addAll(drukOrderList.stream().filter(d -> d.getDate().equals(localDate.toString()) && d.getAssignedTo().equals(persoon)).collect(Collectors.toList()));
        return drukOrderFilteredList;
    }

    public void removeDrukOrder(Integer id) {
        drukOrderService.removeDrukOrder(id);
    }

    public void updateDrukOrder(DrukOrder drukOrder) {
        drukOrderService.updateDrukOrder(drukOrder);
    }

    public void changeDrukOrderToDate(ActionEvent actionEvent) {
        drukOrderFilteredList = getDrukOrderList(drukOrderDatePicker.getValue(), PersoonLabel.getText());

    }

    public void clearAll(ActionEvent actionEvent) {
        drukOrderFilteredList.add(new DrukOrder("Eli", "test"));
    }

    public Label getPersoonLabel() {
        return PersoonLabel;
    }

    public void setPersoonLabel(Label persoonLabel) {
        PersoonLabel = persoonLabel;
    }
}