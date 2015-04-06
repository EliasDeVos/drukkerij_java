package drukkerij.service;

import drukkerij.controller.DrukkerijController;
import drukkerij.model.DrukOrder;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Pol on 19/03/2015.
 */
public class Listener extends Thread {

    private Connection conn;
    private org.postgresql.PGConnection pgconn;
    private DrukkerijController drukkerijController;

    public Listener(Connection conn, String channel) throws SQLException {
        this.conn = conn;
        this.pgconn = (org.postgresql.PGConnection)conn;
        Statement stmtDelete = conn.createStatement();
        stmtDelete.execute("LISTEN "+channel+"drukorder");
        stmtDelete.close();

    }

    public void run() {
        while (true) {
            try {
                // issue a dummy query to contact the backend
                // and receive any pending notifications.
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                rs.close();
                stmt.close();

                org.postgresql.PGNotification notifications[] = pgconn.getNotifications();
                if (notifications != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i=0; i<notifications.length; i++) {


                                if (notifications[i].getName().equals("deletedrukorder"))
                                {
                                    drukkerijController.removeDrukOrderFromList(Integer.parseInt(notifications[i].getParameter()));
                                }
                                if (notifications[i].getName().equals("updatedrukorder"))
                                {
                                    drukkerijController.updateDrukOrderFromList(Integer.parseInt(notifications[i].getParameter()));
                                }
                                if (notifications[i].getName().equals("insertdrukorder"))
                                {
                                    drukkerijController.addDrukOrderToList(Integer.parseInt(notifications[i].getParameter()));
                                }
                            }
                        }
                    });

                }
                // wait a while before checking again for new
                // notifications
                Thread.sleep(500);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


    public void setController(DrukkerijController drukkerijController) {
        this.drukkerijController = drukkerijController;
    }
}
