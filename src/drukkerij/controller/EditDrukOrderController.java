package drukkerij.controller;

import drukkerij.model.DrukOrder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;


/**
 * Created by Pol on 22/03/2015.
 */
public class EditDrukOrderController {

    //region fxml attributes
    @FXML
    private DatePicker drukOrderDatePicker;
    @FXML
    private TextField klantTextField;
    @FXML
    private TextField opdrachtTextField;
    @FXML
    private TextField xPerVelTextField;
    @FXML
    private TextField aantalNodigTextField;
    @FXML
    private TextField inschietTextField;
    @FXML
    private TextField nmkBNTextField;
    @FXML
    private ChoiceBox opdrachtVoorChoiceBox;
    @FXML
    private TextField qTextField;
    @FXML
    private TextField zWTextField;
    @FXML
    private TextField zwaar4Z2TextField;
    @FXML
    private TextField glanzendTextField;
    @FXML
    private TextField helderheidTextField;
    @FXML
    private ChoiceBox soortPapierChoiceBox;
    @FXML
    private ChoiceBox geplaatstDoorChoiceBox;
    @FXML
    private TextField printerTextField;
    //endregion

    private Stage dialogStage;
    public DrukOrder drukOrder;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     *
     * @param drukOrder
     */
    public void setPerson(DrukOrder drukOrder) {
        this.drukOrder = drukOrder;
        //set attributes from drukOrder in textField


    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
           //set textfields in drukOrder object

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        //validate

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
