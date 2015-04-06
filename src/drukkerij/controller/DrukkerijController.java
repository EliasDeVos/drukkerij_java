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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import org.controlsfx.control.Notifications;

import java.sql.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
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
    private Label xPerVelInfoLabel;
    @FXML
    private Label aantalNodigInfoLabel;
    @FXML
    private Label inschietInfoLabel;
    @FXML
    private Label nmkNBInfoLabel;
    @FXML
    private Label qInfoLabel;
    @FXML
    private Label zwInfoLabel;
    @FXML
    private Label zwaar4Z2InfoLabel;
    @FXML
    private Label glanzendInfoLabel;
    @FXML
    private Label helderheidInfoLabel;
    @FXML
    private Label soortPapierInfoLabel;
    @FXML
    private Label geplaatstDoorInfoLabel;
    @FXML
    private Label printerInfoLabel;
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
    private TableColumn<DrukOrder, String> prioriteitDruk;
    @FXML
    private TableColumn<DrukOrder, String> typeDruk;
    @FXML
    private DatePicker drukOrderDatePicker;
    @FXML
    private Label PersoonLabel;
    @FXML
    private Label drukOrderHeaderLabel;
    //endregion


    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        klantDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("klant"));
        opdrachtDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("opdracht"));
        typeDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("type"));
        prioriteitDruk.setCellValueFactory(new PropertyValueFactory<>("prioriteit"));
        prioriteitDruk.setComparator(comparatorDrukOrderPrioriteit);

        drukOrderTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDrukOrderDetails(newValue));
    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        drukOrderTable.setItems(getDrukOrderFilteredList(LocalDate.now(), PersoonLabel.getText()));
        drukOrderDatePicker.setValue(LocalDate.now());
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
            Listener listenerDelete = new Listener(lConn, "delete");
            listenerDelete.start();
            listenerDelete.setController(this);
            Listener listenerInsert = new Listener(lConn, "insert");
            listenerInsert.start();
            listenerInsert.setController(this);
            Listener listenerUpdate = new Listener(lConn, "update");
            listenerUpdate.start();
            listenerUpdate.setController(this);
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
        tempDrukOrder.setDate((LocalDate.now()).toString());
        tempDrukOrder.setPrinter("Xerox 560");
        boolean okClicked = mainApp.showDrukOrderEditDialog(tempDrukOrder, "new");
        if (okClicked) {
            addDrukOrder(tempDrukOrder);
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
            boolean okClicked = mainApp.showDrukOrderEditDialog(selectedDrukOrder, "edit");
            if (okClicked) {
                updateDrukOrder(selectedDrukOrder);
                getDrukOrderFilteredList().remove(selectedDrukOrder);
                showDrukOrderDetails(selectedDrukOrder);
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Geen selectie");
            alert.setHeaderText("Geen drukorder geselecteerd");
            alert.setContentText("Selecteer een drukorder in de tabel om te wijzigen");

            alert.showAndWait();
        }
    }

    public void showDrukOrderDetails(DrukOrder drukorder)
    {
        if (drukorder != null && drukorder.getType().equalsIgnoreCase("drukorder"))
        {
            xPerVelLabel.setText("X per vel");
            aantalNodigLabel.setText("Aantal nodig");
            inschietLabel.setText("Inschiet");
            nmkNBLabel.setText("NmkNB");
            qLabel.setText("Q");
            zwLabel.setText("Z/W");
            zwaar4Z2Label.setText("Zwaar 4Z2");
            glanzendLabel.setText("Glanzend");
            helderheidLabel.setText("Helderheid");
            soortPapierLabel.setText("Soort papier");
            geplaatstDoorLabel.setText("Geplaatsts door");
            printerLabel.setText("Printer");
            xPerVelInfoLabel.setText(drukorder.getxPerVel());
            aantalNodigInfoLabel.setText(drukorder.getAantalNodig());
            inschietInfoLabel.setText(drukorder.getInschiet());
            nmkNBInfoLabel.setText(drukorder.getNmkNB());
            qInfoLabel.setText(drukorder.getQ());
            zwInfoLabel.setText(drukorder.getzW());
            zwaar4Z2InfoLabel.setText(drukorder.getZwaar4Z2());
            glanzendInfoLabel.setText(drukorder.getGlanzend());
            helderheidInfoLabel.setText(drukorder.getHelderheid());
            soortPapierInfoLabel.setText(drukorder.getSoortPapier());
            geplaatstDoorInfoLabel.setText(drukorder.getGeplaatstDoor());
            printerInfoLabel.setText(drukorder.getPrinter());
            drukOrderHeaderLabel.setText(drukorder.getOpdracht() + " voor " + drukorder.getKlant() + " op " + drukorder.getDate() + " van type " + drukorder.getType());
        }
        else if (drukorder!= null)
        {
            xPerVelLabel.setText("");
            aantalNodigLabel.setText("");
            inschietLabel.setText("");
            nmkNBLabel.setText("");
            qLabel.setText("");
            zwLabel.setText("");
            zwaar4Z2Label.setText("");
            glanzendLabel.setText("");
            helderheidLabel.setText("");
            soortPapierLabel.setText("");
            geplaatstDoorLabel.setText("");
            printerLabel.setText("");
            xPerVelInfoLabel.setText("");
            aantalNodigInfoLabel.setText("");
            inschietInfoLabel.setText("");
            nmkNBInfoLabel.setText("");
            qInfoLabel.setText("");
            zwInfoLabel.setText("");
            zwaar4Z2InfoLabel.setText("");
            glanzendInfoLabel.setText("");
            helderheidInfoLabel.setText("");
            soortPapierInfoLabel.setText("");
            geplaatstDoorInfoLabel.setText("");
            printerInfoLabel.setText("");
            drukOrderHeaderLabel.setText(drukorder.getOpdracht() + " voor " + drukorder.getKlant() + " op " + drukorder.getDate() + " van type " + drukorder.getType());
        }
    }


    public void addDrukOrder(DrukOrder drukOrder) {
        drukOrderService.addDrukOrder(drukOrder);
    }

    public ObservableList<DrukOrder> getDrukOrderFilteredList(LocalDate localDate, String persoon) {
        if (drukOrderFilteredList.isEmpty())
        {
            drukOrderList = FXCollections.observableList((List<DrukOrder>) drukOrderService.listDrukOrder());
        }
        drukOrderFilteredList.clear();
        drukOrderFilteredList.addAll(drukOrderList.stream().filter(d -> d.getDate().equals(localDate.toString()) && d.getOpdrachtVoor().equals(persoon)).collect(Collectors.toList()));
        return drukOrderFilteredList;
    }

    public ObservableList<DrukOrder> getDrukOrderList() {
        if (drukOrderList.isEmpty())
        {
            drukOrderList = FXCollections.observableList((List<DrukOrder>) drukOrderService.listDrukOrder());
        }
        return drukOrderList;
    }

    public ObservableList<DrukOrder> getDrukOrderFilteredList() {
        if (drukOrderFilteredList.isEmpty())
        {
            drukOrderFilteredList = FXCollections.observableList((List<DrukOrder>) drukOrderService.listDrukOrder());
        }
        return drukOrderFilteredList;
    }

    public void removeDrukOrder(Integer id) {
        drukOrderService.removeDrukOrder(id);
    }

    public void updateDrukOrder(DrukOrder drukOrder) {
        drukOrderService.updateDrukOrder(drukOrder);
    }

    public void changeDrukOrderToDate(ActionEvent actionEvent) {
        drukOrderFilteredList = getDrukOrderFilteredList(drukOrderDatePicker.getValue(), PersoonLabel.getText());
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

    public void removeDrukOrderFromList(int drukOrderId) {
        DrukOrder tempDrukOrder = null;
        for (DrukOrder drukOrder : drukOrderList)
        {
            if (drukOrder.getDrukOrderId() == drukOrderId)
            {
                tempDrukOrder = drukOrder;
            }
        }
        drukOrderFilteredList.remove(tempDrukOrder);
    }

    public void updateDrukOrderFromList(int drukOrderId) {
        //TODO nu wordt heel de lijst geleegd zodat we de nieuwe data hebben, moeten eig alleen geupdate drukorder uit de db halen
        DrukOrder tempDrukOrder = null;
        for (DrukOrder drukOrder : getDrukOrderList())
        {
            if (drukOrder.getDrukOrderId() == drukOrderId)
            {
                tempDrukOrder = drukOrder;
            }
        }
        getDrukOrderFilteredList().remove(tempDrukOrder);
        tempDrukOrder = null;
        drukOrderList.clear();
        for (DrukOrder drukOrder : getDrukOrderList())
        {
            if (drukOrder.getDrukOrderId() == drukOrderId)
            {
                tempDrukOrder = drukOrder;
                if (tempDrukOrder.getPrioriteit().equals("Hoog"))
                {
                    Notifications.create()
                            .title("Prioriteit drukorder")
                            .text("Druk order met hoge prioriteit op datum \n" + tempDrukOrder.getDate() + " voor " + tempDrukOrder.getOpdrachtVoor())
                            .position(Pos.TOP_RIGHT)
                            .showInformation();
                }
            }
        }

        if (drukOrderDatePicker.getValue().toString().equals(tempDrukOrder.getDate()) && getPersoonLabel().getText().equals(tempDrukOrder.getOpdrachtVoor()))
        {
            getDrukOrderFilteredList().add(tempDrukOrder);
            drukOrderTable.getColumns().get(0).setVisible(false);
            drukOrderTable.getColumns().get(0).setVisible(true);
        }
        else
        {
            drukOrderList.add(tempDrukOrder);
        }

    }

    public void addDrukOrderToList(int drukOrderId) {
        //TODO zie updateDrukORderFromList
        DrukOrder tempDrukOrder = null;
        drukOrderList.clear();
        for (DrukOrder drukOrder : getDrukOrderList())
        {
            if (drukOrder.getDrukOrderId() == drukOrderId)
            {
                tempDrukOrder = drukOrder;
                if (tempDrukOrder.getPrioriteit().equals("Hoog"))
                {
                    Notifications.create()
                            .title("Prioriteit drukorder")
                            .text("Druk order met hoge prioriteit op datum \n" + tempDrukOrder.getDate() + " voor " + tempDrukOrder.getOpdrachtVoor())
                            .position(Pos.TOP_RIGHT)
                            .showInformation();
                }
            }
        }
        if (drukOrderDatePicker.getValue().toString().equals(tempDrukOrder.getDate()) && getPersoonLabel().getText().equals(tempDrukOrder.getOpdrachtVoor()))
        {
            getDrukOrderFilteredList().add(tempDrukOrder);
            drukOrderTable.getColumns().get(0).setVisible(false);
            drukOrderTable.getColumns().get(0).setVisible(true);
        }
        else
        {
            drukOrderList.add(tempDrukOrder);
        }
    }

    Comparator<String> comparatorDrukOrderPrioriteit = new Comparator<String>() {
        @Override
        public int compare(String drukOrder1, String drukOrder2) {
            if (drukOrder1.equals("Hoog"))
            {
                return 1;
            }
            if (drukOrder2.equals("Hoog"))
            {
                return 0;
            }
            if (drukOrder1.equals("normaal"))
            {
                return 1;
            }
            if (drukOrder2.equals("normaal"))
            {
                return 0;
            }
            return 1;
        }
    };

    public void handleNewItem() {
        mainApp.showAddDrukItem(this);

    }
}