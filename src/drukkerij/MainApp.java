package drukkerij;

import drukkerij.controller.*;
import drukkerij.model.DrukItem;
import drukkerij.model.HibernateUtil;
import drukkerij.model.Host;
import drukkerij.service.Listener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.applet.Main;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Listener listenerDelete;
    private Listener listenerUpdate;
    private Listener listenerInsert;
    private String hostname = "";
    public static String hostnameStatic;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Drukkerij Alphabet");
        loadHostnameFromFile();

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/alfaLogo.PNG")));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        System.exit(0);
                    }
                });
            }
        });
        initRootWelcomeLayout();
        showWelcomeView();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(primaryStage);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root layout.
     */
    public void initRootWelcomeLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayoutWelcome.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutWelcomeController controller = loader.getController();
            controller.setMainApp(this);
            controller.setStage(primaryStage);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDrukOrdersOverview(String person, String date) {
        try {
            // Load person overview.
            primaryStage.setMaximized(true);
            initRootLayout();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MainView.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the drukkerij.controller access to the main app.
            DrukkerijController controller = loader.getController();
            listenerDelete.setController(controller);
            listenerUpdate.setController(controller);
            listenerInsert.setController(controller);
            controller.getPersoonLabel().setText(person);
            controller.setMainApp(this, date);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showWelcomeView() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Welcome.fxml"));
            AnchorPane welcomeView = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(welcomeView);

            // Give the drukkerij.controller access to the main app.
            WelcomeController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddDrukItem(DrukkerijController drukkerijController) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/addDrukItem.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add item");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            AddDrukItemController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setDrukkerijController(drukkerijController);


            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showDrukOrderEditDialog(DrukItem drukItem, String type) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/editDrukOrder.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(type + " Drukorder");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EditDrukOrderController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDrukItem(drukItem, type);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createListeners() throws Exception
    {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + hostname + ":5432/testdb";

            // Create two distinct connections, one for the notifier
            // and another for the listener to show the communication
            // works across connections although this example would
            // work fine with just one connection.
            Connection lConn = null;
            int i = 0;
            while (i < 5 && lConn == null)
            {
                lConn = DriverManager.getConnection(url, "postgres", "root");
            }

            if (lConn == null)
            {
                System.exit(0);
            }
            // Create two threads, one to issue notifications and
            // the other to receive them.
            listenerDelete = new Listener(lConn, "delete");
            listenerDelete.start();
            listenerInsert = new Listener(lConn, "insert");
            listenerInsert.start();
            listenerUpdate = new Listener(lConn, "update");
            listenerUpdate.start();
    }

    public MainApp() {
        // Add some sample data
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(5423);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        launch(args);
    }


    public void showSearchDrukItemView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/searchDrukItemView.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Drukitem zoeken");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            SearchDrukItemController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();


        } catch (IOException e) {

        }

    }

    public boolean showOpmaakPlaat(DrukItem tempDrukItem, String aNew, String type) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/editOpmaakPlaat.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(aNew + " " + type);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EditOpmaakPlaatController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDrukItem(tempDrukItem, aNew, type);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     */
    public void loadHostnameFromFile() {
        try {
            File file = new File(".\\src\\drukkerij\\img\\host.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Host.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Host host = (Host) jaxbUnmarshaller.unmarshal(file);
            hostname = host.getHostname();

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n");

            alert.showAndWait();
        }
    }

    /**
     * Saves the current host data to the specified file.
     *
     */
    public void saveHostDataToFile() {
        try {
            File file = new File(".\\src\\drukkerij\\img\\host.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Host.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            Host host = new Host();
            host.setHostname(hostname);

            jaxbMarshaller.marshal(host, file);

            // Save the file path to the registry.
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n");

            alert.showAndWait();
        }
    }

    public boolean showEditHostname()
    {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EditHost.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("edit host");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EditHostController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setHostname(hostname);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setStaticHostname(String hostname)
    {
        MainApp.hostnameStatic = hostname;
    }
}