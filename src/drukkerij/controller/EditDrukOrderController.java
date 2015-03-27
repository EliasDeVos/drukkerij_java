package drukkerij.controller;

import drukkerij.model.DrukOrder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;


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
    private ComboBox opdrachtVoorComboBox;
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
    private ComboBox soortPapierComboBox;
    @FXML
    private ComboBox geplaatstDoorComboBox;
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
    public void setDrukOrder(DrukOrder drukOrder) {
        printerTextField.setText(drukOrder.getPrinter());
        klantTextField.setText(drukOrder.getKlant());
        opdrachtTextField.setText(drukOrder.getOpdracht());
        drukOrderDatePicker.setValue(LocalDate.parse(drukOrder.getDate()));
        xPerVelTextField.setText(drukOrder.getxPerVel());
        aantalNodigTextField.setText(drukOrder.getAantalNodig());
        inschietTextField.setText(drukOrder.getInschiet());
        nmkBNTextField.setText(drukOrder.getNmkNB());
        opdrachtVoorComboBox.setValue(drukOrder.getOpdrachtVoor());
        qTextField.setText(drukOrder.getQ());
        zWTextField.setText(drukOrder.getzW());
        zwaar4Z2TextField.setText(drukOrder.getZwaar4Z2());
        glanzendTextField.setText(drukOrder.getGlanzend());
        helderheidTextField.setText(drukOrder.getHelderheid());
        soortPapierComboBox.setValue(drukOrder.getSoortPapier());
        geplaatstDoorComboBox.setValue(drukOrder.getGeplaatstDoor());
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
            drukOrder.setPrinter(printerTextField.getText());
            drukOrder.setKlant(klantTextField.getText());
            drukOrder.setOpdracht(opdrachtTextField.getText());
            drukOrder.setDate(drukOrderDatePicker.getValue().toString());
            drukOrder.setxPerVel(xPerVelTextField.getText());
            drukOrder.setAantalNodig(aantalNodigTextField.getText());
            drukOrder.setInschiet(inschietTextField.getText());
            drukOrder.setNmkNB(nmkBNTextField.getText());
            drukOrder.setOpdrachtVoor(opdrachtVoorComboBox.getSelectionModel().getSelectedItem().toString());
            drukOrder.setQ(qTextField.getText());
            drukOrder.setzW(zWTextField.getText());
            drukOrder.setZwaar4Z2(zwaar4Z2TextField.getText());
            drukOrder.setGlanzend(glanzendTextField.getText());
            drukOrder.setHelderheid(helderheidTextField.getText());
            drukOrder.setSoortPapier(soortPapierComboBox.getSelectionModel().getSelectedItem().toString());
            drukOrder.setGeplaatstDoor(geplaatstDoorComboBox.getSelectionModel().getSelectedItem().toString());
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
        String regExDate = "([0-9]{4}-[0-9]{2}-[0-9]{2})";
        if (!drukOrderDatePicker.getValue().toString().matches(regExDate))
        {
            errorMessage += "Voeg een correcte datum in";
        }
        if (klantTextField.getText().length() == 0 || opdrachtTextField.getText().length() == 0)
        {
            errorMessage += "\nKlant en opdracht mogen niet leeg zijn";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Foute input");
            alert.setHeaderText("Vul de juiste velden in");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
