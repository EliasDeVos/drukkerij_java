package drukkerij.service;

import drukkerij.model.DrukOrder;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

/**
 *
 * @author Elias
 */
public class FormattedTableCellFactory implements Callback<TableColumn, TableCell> {

    @Override
    public TableCell call(TableColumn p) {

        TableCell cell = new TableCell<DrukOrder, Object>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : getString());
                setGraphic(null);
                TableRow currentRow = getTableRow();
                DrukOrder currentDrukOrder = currentRow == null ? null : (DrukOrder)currentRow.getItem();
                if(currentDrukOrder != null){
                    String priority = currentDrukOrder.getPrioriteit();
                    clearPriorityStyle();
                    setPriorityStyle(priority);

                }
            }

            @Override
            public void updateSelected(boolean upd){
                super.updateSelected(upd);
            }

            private void clearPriorityStyle(){
                ObservableList<String> styleClasses = getStyleClass();
                styleClasses.remove("priorityLow");
                styleClasses.remove("priorityMedium");
                styleClasses.remove("priorityHigh");
            }

            private void setPriorityStyle(String priority){
                switch(priority){
                    case "laag":
                        getStyleClass().add("priorityLow");
                        break;
                    case "normaal":
                        getStyleClass().add("priorityMedium");
                        break;
                    case "Hoog":
                        getStyleClass().add("priorityHigh");
                        break;
                }
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };
        return cell;
    } }