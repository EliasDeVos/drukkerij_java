package drukkerij.controller;

import drukkerij.MainApp;
import javafx.event.ActionEvent;

/**
 * Created by Pol on 22/03/2015.
 */
public class RootLayoutController {

    public MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void showDrukOrderMartine()
    {
        mainApp.showDrukOrdersOverviewMartine();
    }

    public void showDrukOrderJo() {
        mainApp.showDrukOrdersOverviewJo();
    }
}
