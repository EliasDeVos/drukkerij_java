package drukkerij.controller;

import drukkerij.MainApp;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * Created by Pol on 22/03/2015.
 */
public class RootLayoutController {

    public MainApp mainApp;
    private Stage stage;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void showDrukOrderMartine() {
        mainApp.showDrukOrdersOverview("Martine");
    }

    public void showDrukOrderJo() {
        mainApp.showDrukOrdersOverview("Jo");
    }

    public void handleClose(ActionEvent actionEvent) {
        stage.close();
    }

    public void setStage(Stage primaryStage) {
        this.stage = stage;
    }
}
