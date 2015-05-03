package drukkerij.controller;

import drukkerij.MainApp;
import drukkerij.model.DrukItem;
import drukkerij.service.DrukItemService;
import drukkerij.service.DrukItemServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Created by Fre on 5/04/2015.
 */
public class SearchDrukItemController {
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DrukItem> drukItemTable;
    @FXML
    private TableColumn<DrukItem, String> klantDruk;
    @FXML
    private TableColumn<DrukItem, String> opdrachtDruk;
    @FXML
    private TableColumn<DrukItem, String> typeDruk;

    private ObservableList<DrukItem> masterData = FXCollections.observableArrayList();
    private DrukItemService drukItemService = new DrukItemServiceImpl();
    private MainApp mainApp;
    private Stage stage;

    /**
     * Just add some sample data in the constructor.
     */
    public SearchDrukItemController() {
        masterData.addAll(drukItemService.listDrukOrder());
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * <p/>
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {
        // 0. Initialize the columns.
        // Initialize the person table with the two columns.
        klantDruk.setCellValueFactory(new PropertyValueFactory<DrukItem, String>("klant"));
        opdrachtDruk.setCellValueFactory(new PropertyValueFactory<DrukItem, String>("opdracht"));
        typeDruk.setCellValueFactory(new PropertyValueFactory<DrukItem, String>("type"));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<DrukItem> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(drukOrder -> {
                // If filter text is empty, display all drukorder
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (drukOrder.getOpdracht().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (drukOrder.getKlant().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<DrukItem> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(drukItemTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        drukItemTable.setItems(sortedData);
    }

    public void handleNextButtonClick(ActionEvent actionEvent) {
        DrukItem selectedDrukItem = drukItemTable.getSelectionModel().getSelectedItem();
        if (selectedDrukItem != null) {
            selectedDrukItem.setDate((LocalDate.now()).toString());
            if (selectedDrukItem.getType().equalsIgnoreCase("drukorder")) {
                boolean okClicked = mainApp.showDrukOrderEditDialog(selectedDrukItem, "edit");
                if (okClicked) {
                    saveDrukOrder(selectedDrukItem);
                    stage.close();
                }
            } else if (selectedDrukItem.getType().equalsIgnoreCase("plaat")) {
                boolean okClicked = mainApp.showOpmaakPlaat(selectedDrukItem, "edit", "plaat");
                if (okClicked) {
                    saveDrukOrder(selectedDrukItem);
                    stage.close();
                }
            } else if (selectedDrukItem.getType().equalsIgnoreCase("opmaak")) {
                boolean okClicked = mainApp.showOpmaakPlaat(selectedDrukItem, "edit", "opmaak");
                if (okClicked) {
                    saveDrukOrder(selectedDrukItem);
                    stage.close();
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

    public void saveDrukOrder(DrukItem drukItem) {
        drukItemService.addDrukOrder(drukItem);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.stage = dialogStage;
    }
}
