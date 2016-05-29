package drukkerij.controller;

import drukkerij.model.DrukItem;
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
    private TextField soortPapierTextField;
    @FXML
    private ComboBox geplaatstDoorComboBox;
    @FXML
    private TextField printerTextField;
    @FXML
    private ComboBox prioriteitComboBox;
    @FXML
    private TextArea commentaarTextArea;
    //endregion

    private Stage dialogStage;
    public DrukItem drukItem;
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
     * @param drukItem
     * @param type
     */
    public void setDrukItem(DrukItem drukItem, String type) {
        this.drukItem = drukItem;
        printerTextField.setText(drukItem.getPrinter());
        drukOrderDatePicker.setValue(LocalDate.parse(drukItem.getDate()));
        if (type.equals("edit")) {
            klantTextField.setText(drukItem.getKlant());
            opdrachtTextField.setText(drukItem.getOpdracht());
            xPerVelTextField.setText(drukItem.getxPerVel());
            aantalNodigTextField.setText(drukItem.getAantalNodig());
            inschietTextField.setText(drukItem.getInschiet());
            nmkBNTextField.setText(drukItem.getNmkNB());
            opdrachtVoorComboBox.setValue(drukItem.getOpdrachtVoor());
            qTextField.setText(drukItem.getQ());
            zWTextField.setText(drukItem.getzW());
            zwaar4Z2TextField.setText(drukItem.getZwaar4Z2());
            glanzendTextField.setText(drukItem.getGlanzend());
            helderheidTextField.setText(drukItem.getHelderheid());
            soortPapierTextField.setText(drukItem.getSoortPapier());
            geplaatstDoorComboBox.setValue(drukItem.getGeplaatstDoor());
            prioriteitComboBox.setValue(drukItem.getPrioriteit());
            commentaarTextArea.setText(drukItem.getCommentaar());

        }
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
            drukItem.setPrinter(printerTextField.getText());
            drukItem.setKlant(klantTextField.getText());
            drukItem.setOpdracht(opdrachtTextField.getText());
            drukItem.setDate(drukOrderDatePicker.getValue().toString());
            drukItem.setxPerVel(xPerVelTextField.getText());
            drukItem.setAantalNodig(aantalNodigTextField.getText());
            drukItem.setInschiet(inschietTextField.getText());
            drukItem.setNmkNB(nmkBNTextField.getText());
            drukItem.setOpdrachtVoor(opdrachtVoorComboBox.getSelectionModel().getSelectedItem().toString());
            drukItem.setQ(qTextField.getText());
            drukItem.setzW(zWTextField.getText());
            drukItem.setZwaar4Z2(zwaar4Z2TextField.getText());
            drukItem.setGlanzend(glanzendTextField.getText());
            drukItem.setHelderheid(helderheidTextField.getText());
            drukItem.setSoortPapier(soortPapierTextField.getText());
            drukItem.setGeplaatstDoor(geplaatstDoorComboBox.getSelectionModel().getSelectedItem().toString());
            drukItem.setPrioriteit(prioriteitComboBox.getSelectionModel().getSelectedItem().toString());
            drukItem.setCommentaar(commentaarTextArea.getText());
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
        if (!drukOrderDatePicker.getValue().toString().matches(regExDate)) {
            errorMessage += "Voeg een correcte datum in";
        }
        if (klantTextField.getText().length() == 0 || opdrachtTextField.getText().length() == 0) {
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
