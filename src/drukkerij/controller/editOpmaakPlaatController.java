package drukkerij.controller;

import drukkerij.model.DrukItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Created by Pol on 8/04/2015.
 */
public class EditOpmaakPlaatController {
    @FXML
    private Label headerLabel;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private TextField klantTextField;
    @FXML
    private TextField opdrachtTextField;
    @FXML
    private TextArea commentaarTextArea;
    @FXML
    private ComboBox opdrachtVoorComboBox;
    @FXML
    private ComboBox prioriteitComboBox;

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
    public void setDrukItem(DrukItem drukItem, String aNew, String type) {
        this.drukItem = drukItem;
        dateDatePicker.setValue(LocalDate.parse(drukItem.getDate()));
        headerLabel.setText(aNew + " " + type);
        if (aNew.equals("edit")) {
            klantTextField.setText(drukItem.getKlant());
            opdrachtTextField.setText(drukItem.getOpdracht());
            commentaarTextArea.setText(drukItem.getCommentaar());
            opdrachtVoorComboBox.setValue(drukItem.getOpdrachtVoor());
            prioriteitComboBox.setValue(drukItem.getPrioriteit());
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
            drukItem.setKlant(klantTextField.getText());
            drukItem.setOpdracht(opdrachtTextField.getText());
            drukItem.setCommentaar(commentaarTextArea.getText());
            drukItem.setOpdrachtVoor(opdrachtVoorComboBox.getValue().toString());
            drukItem.setPrioriteit(prioriteitComboBox.getValue().toString());
            drukItem.setDate(dateDatePicker.getValue().toString());
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
        if (!dateDatePicker.getValue().toString().matches(regExDate)) {
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

    public Label getHeaderLabel() {
        return headerLabel;
    }
}
