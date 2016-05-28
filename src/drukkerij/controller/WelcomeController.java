package drukkerij.controller;

import drukkerij.MainApp;
import drukkerij.model.HibernateUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Created by Pol on 9/11/2015.
 */
public class WelcomeController {

    @FXML
    private Label hostname;
    private MainApp mainApp;

    public WelcomeController()
    {
    }

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
        hostname.setText(mainApp.getHostname());
    }

    public void createOverviewMartine(Event event)
    {
        try
        {
            mainApp.setStaticHostname(mainApp.getHostname());
            mainApp.createListeners();


        }catch(Exception e)
        {
            //Alert alert = new Alert(Alert.AlertType.ERROR);
            e.printStackTrace();
            //alert.setTitle("Error");
            //alert.setHeaderText("Cant connect to db");
            //alert.setContentText("Kon niet connecteren met db met hostname"  + MainApp.hostnameStatic);

            //alert.showAndWait();
            //System.exit(0);
        }

        mainApp.showDrukOrdersOverview("Martine", null);
    }

    public void createOverviewJo(Event event)
    {
        try
        {
            mainApp.setStaticHostname(mainApp.getHostname());
            mainApp.createListeners();

        }catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cant connect to db");
            alert.setContentText("Kon niet connecteren met db met hostname" + MainApp.hostnameStatic);

            alert.showAndWait();
            System.exit(0);
        }

        mainApp.showDrukOrdersOverview("Jo", null);
    }

    public void changeHostname(Event event)
    {
        mainApp.showEditHostname();
        hostname.setText(mainApp.getHostname());
    }
}
