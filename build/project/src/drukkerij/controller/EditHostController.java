package drukkerij.controller;

import drukkerij.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Pol on 16/11/2015.
 */
public class EditHostController {

    private Stage dialogStage;
    private boolean okClicked;
    @FXML
    private TextField hostname;
    private MainApp mainApp;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setHostname(String hostname) {
        this.hostname.setText(hostname);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        okClicked = true;
        mainApp.setHostname(hostname.getText());
        mainApp.saveHostDataToFile();
        dialogStage.close();

    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
