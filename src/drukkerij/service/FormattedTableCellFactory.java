package drukkerij.service;

import drukkerij.MainApp;
import drukkerij.model.DrukItem;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * @author Elias
 */
public class FormattedTableCellFactory implements Callback<TableColumn, TableCell> {

    @Override
    public TableCell call(TableColumn p) {

        TableCell cell = new TableCell<DrukItem, Object>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : getString());
                setGraphic(null);
                TableRow currentRow = getTableRow();
                DrukItem currentDrukItem = currentRow == null ? null : (DrukItem) currentRow.getItem();
                clearPriorityStyle();
                if (currentDrukItem != null) {
                    String priority = currentDrukItem.getPrioriteit();
                    setPriorityStyle(priority.toLowerCase());
                    setStyle("-fx-alignment: center;");
                    if (getTableColumn().getId().equalsIgnoreCase("afwerkdruk")) {
                        final ImageView imageview = new ImageView();
                        imageview.setFitHeight(25);
                        imageview.setFitWidth(25);
                        if (getTableRow().getItem() != null) {
                            if (((DrukItem) getTableRow().getItem()).getPrioriteit().equalsIgnoreCase("finished")) {
                                imageview.setImage(new Image(MainApp.class.getResource("img").toString() + "/ok.png"));
                            }
                        }
                        setGraphic(imageview);
                    }

                }
            }

            @Override
            public void updateSelected(boolean upd) {
                super.updateSelected(upd);
            }

            private void clearPriorityStyle() {
                ObservableList<String> styleClasses = getStyleClass();
                styleClasses.remove("priorityLow");
                styleClasses.remove("priorityMedium");
                styleClasses.remove("priorityHigh");
                styleClasses.remove("priorityFinished");
            }

            private void setPriorityStyle(String priority) {
                switch (priority) {
                    case "laag":
                        getStyleClass().add("priorityLow");
                        break;
                    case "normaal":
                        getStyleClass().add("priorityMedium");
                        break;
                    case "hoog":
                        getStyleClass().add("priorityHigh");
                        break;
                    case "finished":
                        getStyleClass().add("priorityFinished");
                        break;
                }
            }

            private String getString() {
                return getItem() == null ? "" : getItem().toString();
            }
        };
        return cell;
    }
}