package drukkerij.controller;

import drukkerij.MainApp;
import drukkerij.model.DrukItem;
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
        DrukItem tempDrukItem = new DrukItem();
        tempDrukItem.setDate((LocalDate.now()).toString());
        tempDrukItem.setType("plaat");
        tempDrukItem.setAfgewerkt("false");
        boolean okClicked = mainApp.showOpmaakPlaat(tempDrukItem, "new", "plaat");
        if (okClicked) {
            drukkerijController.addDrukItem(tempDrukItem);
        }
        dialogStage.close();
    }

    public void newOpmaak(){
        DrukItem tempDrukItem = new DrukItem();
        tempDrukItem.setDate((LocalDate.now()).toString());
        tempDrukItem.setType("opmaak");
        tempDrukItem.setAfgewerkt("false");
        boolean okClicked = mainApp.showOpmaakPlaat(tempDrukItem, "new", "opmaak");
        if (okClicked) {
            drukkerijController.addDrukItem(tempDrukItem);
        }
        dialogStage.close();
    }

    public void newDrukOrder()
    {
        DrukItem tempDrukItem = new DrukItem();
        tempDrukItem.setDate((LocalDate.now()).toString());
        tempDrukItem.setPrinter("Xerox 560");
        tempDrukItem.setType("drukorder");
        tempDrukItem.setAfgewerkt("false");
        boolean okClicked = mainApp.showDrukOrderEditDialog(tempDrukItem, "new");
        if (okClicked) {
            drukkerijController.addDrukItem(tempDrukItem);
        }
        dialogStage.close();
    }

    public void newBestaandDrukOrder()
    {
        mainApp.showSearchDrukItemView();
        dialogStage.close();

    }

    public void setDrukkerijController(DrukkerijController drukkerijController) {
        this.drukkerijController = drukkerijController;
    }
}
