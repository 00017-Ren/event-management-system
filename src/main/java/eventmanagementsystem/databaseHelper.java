/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventmanagementsystem;

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ren
 */
public class databaseHelper {
    
    String dbConnect = "jdbc:sqlite:ems.db";
    
    java.sql.Connection mySqlConnection = null;
    
    public void connectDb(){
        try { 
            mySqlConnection =
                    DriverManager.getConnection(dbConnect);
        } catch (SQLException ex) {
            Logger.getLogger(databaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initializeSchema(){
        connectDb();

        if (mySqlConnection == null){
            Logger.getLogger(databaseHelper.class.getName()).log(Level.SEVERE, "Database connection failed.");
            return;
        }

        // Create SQL statements
        String createEventsTable = "CREATE TABLE IF NOT EXISTS events ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "date TEXT, "
                + "time TEXT, "
                + "description TEXT, "
                + "organiser TEXT, "
                + "venue TEXT"
                + ")";

        String createAttendeesTable = "CREATE TABLE IF NOT EXISTS attendees ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "event_id INTEGER, "
                + "name TEXT, "
                + "email TEXT"
                + ")";

        String createVenuesTable = "CREATE TABLE IF NOT EXISTS venues ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "address TEXT, "
                + "capacity TEXT, "
                + "availability INTEGER DEFAULT 1"
                + ")";

        // Execute using prepared statements
        try (PreparedStatement pstmtEvents = mySqlConnection.prepareStatement(createEventsTable);
             PreparedStatement pstmtAttendees = mySqlConnection.prepareStatement(createAttendeesTable);
             PreparedStatement pstmtVenues = mySqlConnection.prepareStatement(createVenuesTable)) {

            pstmtEvents.execute();
            pstmtAttendees.execute();
            pstmtVenues.execute();
        } catch (SQLException ex) {
            Logger.getLogger(databaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}


