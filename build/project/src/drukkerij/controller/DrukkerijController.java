package drukkerij.controller;


import drukkerij.MainApp;
import drukkerij.model.DrukItem;
import drukkerij.service.DrukItemService;
import drukkerij.service.DrukItemServiceImpl;
import drukkerij.service.Listener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DrukkerijController {
    private MainApp mainApp;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private DrukItemService drukItemService = new DrukItemServiceImpl();
    private ObservableList<DrukItem> drukItemList = FXCollections.observableArrayList();
    private ObservableList<DrukItem> drukItemFilteredList = FXCollections.observableArrayList();

    //region fxml parameters
    @FXML
    private TableView<DrukItem> drukItemTable;
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
    private TableColumn<DrukItem, String> klantDruk;
    @FXML
    private TableColumn<DrukItem, String> opdrachtDruk;
    @FXML
    private TableColumn<DrukItem, String> prioriteitDruk;
    @FXML
    private TableColumn<DrukItem, String> typeDruk;
    @FXML
    private TableColumn<DrukItem, Image> afwerkDruk;
    @FXML
    private DatePicker drukOrderDatePicker;
    @FXML
    private Label PersoonLabel;
    @FXML
    private Label drukOrderHeaderLabel;
    @FXML
    private Label commentaarInfoLabel;
    @FXML
    private TextArea commentaarTextArea;
    @FXML
    private Label opdrachtVoorLabel;
    @FXML
    private Label opdrachtVoorInfo;
    @FXML
    private Label infoLabel;
    //endregion


    @FXML
    private void initialize() {

        klantDruk.setCellValueFactory(new PropertyValueFactory<DrukItem, String>("klant"));
        opdrachtDruk.setCellValueFactory(new PropertyValueFactory<DrukItem, String>("opdracht"));
        typeDruk.setCellValueFactory(new PropertyValueFactory<DrukItem, String>("type"));
        prioriteitDruk.setCellValueFactory(new PropertyValueFactory<>("prioriteit"));
        prioriteitDruk.setComparator(comparatorDrukItemPrioriteit);
        TableColumn<DrukItem, Image> afgewerktColumn = new TableColumn<DrukItem, Image>("Afgewerkt");
        afgewerktColumn.setCellValueFactory(new PropertyValueFactory("image"));

        drukItemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDrukItemDetails(newValue));

    }


    public void setMainApp(MainApp mainApp, String date) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        if (date != null)
        {
            drukOrderDatePicker.setValue(LocalDate.parse(date));
        }
        else
        {
            drukOrderDatePicker.setValue(LocalDate.now());
        }
        drukItemFilteredList.clear();
        drukItemList.clear();
        drukItemTable.setItems(getDrukItemFilteredList(LocalDate.now(), PersoonLabel.getText()));
        prioriteitDruk.setSortType(TableColumn.SortType.DESCENDING);
        drukItemTable.getSortOrder().add(prioriteitDruk);
        showDrukItemDetails(null);
        // Use Drag and drop to sort tableview
        drukItemTable.setRowFactory(tv -> {
            final ContextMenu contextMenuAfgewerkt = new ContextMenu();
            final ContextMenu contextMenuNietAfgewerkt = new ContextMenu();
            final MenuItem afwerkItem = new MenuItem("Afgewerkt");
            afwerkItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleFinishedDrukItem(drukItemTable.getSelectionModel().getSelectedItem());
                }
            });
            final MenuItem undoAfwerkItem = new MenuItem("Undo afgewerkt");
            undoAfwerkItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handeUndoFinishedDrukItem(drukItemTable.getSelectionModel().getSelectedItem());
                }
            });
            final MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleEditDrukItem();
                }
            });
            final MenuItem editItem1 = new MenuItem("Edit");
            editItem1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleEditDrukItem();
                }
            });
            contextMenuAfgewerkt.getItems().addAll(editItem, undoAfwerkItem);
            contextMenuNietAfgewerkt.getItems().addAll(editItem1, afwerkItem);

            TableRow<DrukItem> row = new TableRow<DrukItem>() {
                @Override
                public void updateItem(DrukItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        if (item.getPrioriteit().equalsIgnoreCase("finished")) {
                            setContextMenu(contextMenuAfgewerkt);
                        } else {
                            setContextMenu(contextMenuNietAfgewerkt);
                        }
                    }
                }
            };
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    handleEditDrukItem();
                }
            });

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
                    DrukItem draggedPerson = drukItemTable.getItems().remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = drukItemTable.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    drukItemTable.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    drukItemTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });

            return row;
        });

    }


    public DrukkerijController() {

    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected drukorder.
     */
    @FXML
    private void handleEditDrukItem() {
        DrukItem selectedDrukItem = drukItemTable.getSelectionModel().getSelectedItem();
        if (selectedDrukItem != null) {
            if (selectedDrukItem.getType().equalsIgnoreCase("drukorder")) {
                boolean okClicked = mainApp.showDrukOrderEditDialog(selectedDrukItem, "edit");
                if (okClicked) {
                    updateDrukItem(selectedDrukItem);
                    getDrukItemFilteredList().remove(selectedDrukItem);
                    showDrukItemDetails(selectedDrukItem);
                }
            } else if (selectedDrukItem.getType().equalsIgnoreCase("plaat")) {
                boolean okClicked = mainApp.showOpmaakPlaat(selectedDrukItem, "edit", "plaat");
                if (okClicked) {
                    updateDrukItem(selectedDrukItem);
                    getDrukItemFilteredList().remove(selectedDrukItem);
                    showDrukItemDetails(selectedDrukItem);
                }
            } else if (selectedDrukItem.getType().equalsIgnoreCase("opmaak")) {
                boolean okClicked = mainApp.showOpmaakPlaat(selectedDrukItem, "edit", "opmaak");
                if (okClicked) {
                    updateDrukItem(selectedDrukItem);
                    getDrukItemFilteredList().remove(selectedDrukItem);
                    showDrukItemDetails(selectedDrukItem);
                }
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

    public void showDrukItemDetails(DrukItem drukItem) {
        if (drukItem != null && drukItem.getType().equalsIgnoreCase("drukOrder")) {
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
            commentaarInfoLabel.setText("Commentaar");
            opdrachtVoorInfo.setText("Opdracht voor");
            infoLabel.setText("Druk order info:");
            xPerVelInfoLabel.setText(drukItem.getxPerVel());
            aantalNodigInfoLabel.setText(drukItem.getAantalNodig());
            inschietInfoLabel.setText(drukItem.getInschiet());
            nmkNBInfoLabel.setText(drukItem.getNmkNB());
            qInfoLabel.setText(drukItem.getQ());
            zwInfoLabel.setText(drukItem.getzW());
            zwaar4Z2InfoLabel.setText(drukItem.getZwaar4Z2());
            glanzendInfoLabel.setText(drukItem.getGlanzend());
            helderheidInfoLabel.setText(drukItem.getHelderheid());
            soortPapierInfoLabel.setText(drukItem.getSoortPapier());
            geplaatstDoorInfoLabel.setText(drukItem.getGeplaatstDoor());
            printerInfoLabel.setText(drukItem.getPrinter());
            commentaarTextArea.setVisible(true);
            commentaarTextArea.setText(drukItem.getCommentaar());
            opdrachtVoorLabel.setText(drukItem.getOpdrachtVoor());
            drukOrderHeaderLabel.setText(drukItem.getOpdracht() + " voor " + drukItem.getKlant() + " op " + drukItem.getDate() + " van type " + drukItem.getType());
        } else if (drukItem != null) {
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
            commentaarInfoLabel.setText("");
            commentaarTextArea.setVisible(true);
            opdrachtVoorInfo.setText("Opdracht voor");
            infoLabel.setText(drukItem.getType() + " info:");
            commentaarInfoLabel.setText("Commentaar");
            opdrachtVoorLabel.setText(drukItem.getOpdrachtVoor());
            commentaarTextArea.setText(drukItem.getCommentaar());
            drukOrderHeaderLabel.setText(drukItem.getOpdracht() + " voor " + drukItem.getKlant() + " op " + drukItem.getDate() + " van type " + drukItem.getType());
        } else {
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
            commentaarInfoLabel.setText("");
            opdrachtVoorLabel.setText("");
            commentaarTextArea.setVisible(false);
            drukOrderHeaderLabel.setText("");
            infoLabel.setText("");
            opdrachtVoorInfo.setText("");
        }
    }


    public void addDrukItem(DrukItem drukItem) {
        drukItemService.addDrukOrder(drukItem);
    }

    public ObservableList<DrukItem> getDrukItemFilteredList(LocalDate localDate, String persoon) {
        if (drukItemList.isEmpty()) {
            drukItemList = FXCollections.observableList((List<DrukItem>) drukItemService.listDrukOrder());
        }
        drukItemFilteredList.clear();
        drukItemFilteredList.addAll(drukItemList.stream().filter(d -> d.getDate().equals(localDate.toString()) && d.getOpdrachtVoor().equals(persoon)).collect(Collectors.toList()));
        return drukItemFilteredList;
    }

    public ObservableList<DrukItem> getDrukItemList() {
        if (drukItemList.isEmpty()) {
            drukItemList = FXCollections.observableList((List<DrukItem>) drukItemService.listDrukOrder());
        }
        return drukItemList;
    }

    public ObservableList<DrukItem> getDrukItemFilteredList() {
        if (drukItemList.isEmpty()) {
            drukItemFilteredList = FXCollections.observableList((List<DrukItem>) drukItemService.listDrukOrder());
        }
        return drukItemFilteredList;
    }

    public void removeDrukItem(Integer id) {
        drukItemService.removeDrukOrder(id);
    }

    public DrukItem getDrukItem(Integer drukItemId) {
        return drukItemService.getDrukItem(drukItemId);
    }

    public void updateDrukItem(DrukItem drukItem) {
        try {
            drukItemService.updateDrukOrder(drukItem);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Wijzeging problemen");
            alert.setHeaderText("Iemand heeft dit item verwijderd");
            alert.setContentText("Iemand heeft dit item verwijderd terwijl jij jouw wijzegingen nog niet had doorgevoerd, je wijzegingen worden niet opgeslaan");

            alert.showAndWait();
        }
    }

    public void changeDrukItemToDate() {
        drukItemFilteredList = getDrukItemFilteredList(drukOrderDatePicker.getValue(), PersoonLabel.getText());
        drukItemTable.setItems(drukItemFilteredList);
        prioriteitDruk.setSortType(TableColumn.SortType.DESCENDING);
        drukItemTable.getSortOrder().add(prioriteitDruk);
        showDrukItemDetails(null);
    }

    public Label getPersoonLabel() {
        return PersoonLabel;
    }

    public void setPersoonLabel(Label persoonLabel) {
        PersoonLabel = persoonLabel;
    }

    public void removeDrukItemFromList(int drukItemId) {
        DrukItem tempDrukItem = null;
        for (DrukItem drukItem : drukItemList) {
            if (drukItem.getDrukItemId() == drukItemId) {
                tempDrukItem = drukItem;
            }
        }
        drukItemFilteredList.remove(tempDrukItem);
    }

    public void updateDrukItemFromList(int drukOrderId, String prioriteit) {
        DrukItem tempDrukItem = null;
        for (DrukItem drukItem : getDrukItemList()) {
            if (drukItem.getDrukItemId() == drukOrderId) {
                tempDrukItem = drukItem;
            }
        }
        getDrukItemFilteredList().remove(tempDrukItem);
        drukItemList.remove(tempDrukItem);
        tempDrukItem = getDrukItem(drukOrderId);
        if (prioriteit.length() != 0 && tempDrukItem.getPrioriteit().equalsIgnoreCase("Hoog") && !tempDrukItem.getPrioriteit().equalsIgnoreCase(prioriteit)) {
            final DrukItem finalTempDrukItem = tempDrukItem;
            Notifications.create()
                    .title("Prioriteit drukorder")
                    .text("Druk order met hoge prioriteit op datum \n" + tempDrukItem.getDate() + " voor " + tempDrukItem.getOpdrachtVoor())
                    .position(Pos.TOP_RIGHT)
                    .onAction((new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent arg0) {
                                    if (getPersoonLabel().getText().equalsIgnoreCase(finalTempDrukItem.getOpdrachtVoor())) {
                                        drukOrderDatePicker.setValue(LocalDate.parse(finalTempDrukItem.getDate()));
                                        changeDrukItemToDate();
                                    } else {
                                        mainApp.showDrukOrdersOverview(finalTempDrukItem.getOpdrachtVoor(), finalTempDrukItem.getDate());
                                    }
                                }
                            })
                    )
                    .showInformation();
        }
        drukItemList.add(tempDrukItem);
        if (drukOrderDatePicker.getValue().toString().equals(tempDrukItem.getDate()) && getPersoonLabel().getText().equals(tempDrukItem.getOpdrachtVoor())) {
            drukItemFilteredList.add(tempDrukItem);
            drukItemTable.getColumns().get(0).setVisible(false);
            drukItemTable.getColumns().get(0).setVisible(true);
        }
        drukItemTable.setItems(drukItemFilteredList);
        prioriteitDruk.setSortType(TableColumn.SortType.DESCENDING);
        drukItemTable.getSortOrder().add(prioriteitDruk);
    }

    public void addDrukItemToList(int drukOrderId, String prioriteit) {
        DrukItem tempDrukItem = getDrukItem(drukOrderId);
        if (prioriteit.length() != 0 && tempDrukItem.getPrioriteit().equalsIgnoreCase("Hoog")) {
            Notifications.create()
                    .title("Prioriteit drukorder")
                    .text("Druk order met hoge prioriteit op datum \n" + tempDrukItem.getDate() + " voor " + tempDrukItem.getOpdrachtVoor())
                    .position(Pos.TOP_RIGHT)
                    .onAction((new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent arg0) {
                                    if (getPersoonLabel().getText().equalsIgnoreCase(tempDrukItem.getOpdrachtVoor())) {
                                        drukOrderDatePicker.setValue(LocalDate.parse(tempDrukItem.getDate()));
                                        changeDrukItemToDate();
                                    } else {
                                        mainApp.showDrukOrdersOverview(tempDrukItem.getOpdrachtVoor(), tempDrukItem.getDate());
                                    }
                                }
                            })
                    )
                    .showInformation();
        }
        drukItemList.add(tempDrukItem);
        if (drukOrderDatePicker.getValue().toString().equals(tempDrukItem.getDate()) && getPersoonLabel().getText().equals(tempDrukItem.getOpdrachtVoor())) {
            drukItemFilteredList.add(tempDrukItem);
            drukItemTable.getColumns().get(0).setVisible(false);
            drukItemTable.getColumns().get(0).setVisible(true);
        }
        drukItemTable.setItems(drukItemFilteredList);
        prioriteitDruk.setSortType(TableColumn.SortType.DESCENDING);
        drukItemTable.getSortOrder().add(prioriteitDruk);
    }

    Comparator<String> comparatorDrukItemPrioriteit = new Comparator<String>() {
        @Override
        public int compare(String drukOrder1, String drukOrder2) {
            if (drukOrder1.equalsIgnoreCase("Hoog")) {
                return 1;
            }
            if (drukOrder2.equalsIgnoreCase("Hoog")) {
                return 0;
            }
            if (drukOrder1.equalsIgnoreCase("normaal")) {
                return 1;
            }
            if (drukOrder2.equalsIgnoreCase("normaal")) {
                return 0;
            }
            if (drukOrder1.equalsIgnoreCase("laag")) {
                return 1;
            }
            if (drukOrder2.equalsIgnoreCase("laag")) {
                return 0;
            }
            if (drukOrder1.equalsIgnoreCase("finished")) {
                return 1;
            }
            if (drukOrder2.equalsIgnoreCase("finished")) {
                return 0;
            }
            return 1;
        }
    };

    public void handleNewItem() {
        showDrukItemDetails(null);
        mainApp.showAddDrukItem(this);
    }

    public void handleFinishedDrukItem(DrukItem selectedDrukItem) {
        if (selectedDrukItem != null) {
            if (!selectedDrukItem.getPrioriteit().equalsIgnoreCase("finished")) {
                selectedDrukItem.setPrioriteit("Finished");
                updateDrukItem(selectedDrukItem);
                showDrukItemDetails(selectedDrukItem);
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

    private void handeUndoFinishedDrukItem(DrukItem selectedDrukItem) {
        if (selectedDrukItem != null) {
            if (selectedDrukItem.getPrioriteit().equalsIgnoreCase("finished")) {
                selectedDrukItem.setPrioriteit("normaal");
                updateDrukItem(selectedDrukItem);
                showDrukItemDetails(selectedDrukItem);
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

}