package drukkerij.controller;

import drukkerij.MainApp;
import drukkerij.model.DrukOrder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Created by Pol on 5/04/2015.
 */
public class AddDrukItemController {

    private MainApp mainApp;
    private Stage dialogStage;
    private DrukkerijController drukkerijController;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void newPlaat()
    {

    }

    public void newOpmaak(){

    }

    public void newDrukOrder()
    {
        DrukOrder tempDrukOrder = new DrukOrder();
        tempDrukOrder.setDate((LocalDate.now()).toString());
        tempDrukOrder.setPrinter("Xerox 560");
        tempDrukOrder.setType("drukorder");
        boolean okClicked = mainApp.showDrukOrderEditDialog(tempDrukOrder, "new");
        if (okClicked) {
            drukkerijController.addDrukOrder(tempDrukOrder);
            drukkerijController.getDrukOrderList().add(tempDrukOrder);
            drukkerijController.addDrukOrderToList(tempDrukOrder.getDrukOrderId());
        }
        dialogStage.close();
    }

    public void newBestaandDrukOrder()
    {
        mainApp.showSearchDrukItemView();

    }

    public void setDrukkerijController(DrukkerijController drukkerijController) {
        this.drukkerijController = drukkerijController;
    }
}
