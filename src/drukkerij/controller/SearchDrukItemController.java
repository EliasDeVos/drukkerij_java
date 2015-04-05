package drukkerij.controller;

import drukkerij.model.DrukOrder;
import drukkerij.service.DrukOrderService;
import drukkerij.service.DrukOrderServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Fre on 5/04/2015.
 */
public class SearchDrukItemController {
    @FXML
    private TextField filterField;
    @FXML
    private TableView<DrukOrder> drukItemTable;
    @FXML
    private TableColumn<DrukOrder, String> klantDruk;
    @FXML
    private TableColumn<DrukOrder, String> opdrachtDruk;
    @FXML
    private TableColumn<DrukOrder, String> typeDruk;

    private ObservableList<DrukOrder> masterData = FXCollections.observableArrayList();
    private DrukOrderService drukOrderService = new DrukOrderServiceImpl();

    /**
     * Just add some sample data in the constructor.
     */
    public SearchDrukItemController() {
        masterData.addAll(drukOrderService.listDrukOrder());
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {
        // 0. Initialize the columns.
        // Initialize the person table with the two columns.
        klantDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("klant"));
        opdrachtDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("opdracht"));
        typeDruk.setCellValueFactory(new PropertyValueFactory<DrukOrder, String>("type"));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<DrukOrder> filteredData = new FilteredList<>(masterData, p -> true);

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
        SortedList<DrukOrder> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(drukItemTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        drukItemTable.setItems(sortedData);
    }
}
