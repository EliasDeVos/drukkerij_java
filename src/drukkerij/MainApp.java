package drukkerij;

import drukkerij.controller.*;
import drukkerij.model.DrukItem;
import drukkerij.service.Listener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Listener listenerDelete;
    private Listener listenerUpdate;
    private Listener listenerInsert;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Drukkerij Alphabet");
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://Eva_De_Vos:5432/testdb";

            // Create two distinct connections, one for the notifier
            // and another for the listener to show the communication
            // works across connections although this example would
            // work fine with just one connection.
            Connection lConn = DriverManager.getConnection(url, "postgres", "root");
            Connection nConn = DriverManager.getConnection(url, "postgres", "root");

            // Create two threads, one to issue notifications and
            // the other to receive them.
            listenerDelete = new Listener(lConn, "delete");
            listenerDelete.start();
            listenerInsert = new Listener(lConn, "insert");
            listenerInsert.start();
            listenerUpdate = new Listener(lConn, "update");
            listenerUpdate.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/alfaLogo.PNG")));
        primaryStage.setMaximized(true);
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
        initRootLayout();
        showDrukOrdersOverview("Jo");
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

    public void showDrukOrdersOverview(String person) {
        try {
            // Load person overview.
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
            dialogStage.setTitle("Edit Drukorder");
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
}