package drukkerij.controller;

import drukkerij.MainApp;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * Created by Pol on 22/03/2015.
 */
public class RootLayoutWelcomeController {

    public MainApp mainApp;
    private Stage stage;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void handleClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setStage(Stage primaryStage) {
        this.stage = stage;
    }
}
