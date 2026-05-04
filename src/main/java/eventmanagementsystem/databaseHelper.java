/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventmanagementsystem;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ren
 */
public class databaseHelper {
    
    String dbConnect = "jdbc:mysql://localhost:3306/ems";
    String dbUser = "root";
    String dbPassword = "123456";
    
    java.sql.Connection mySqlConnection = null;
    
    //Statement statement = null;
    
    public void connectDb(){
        try { 
            mySqlConnection =
                    DriverManager.getConnection(dbConnect, dbUser, dbPassword);
            //statement = mySqlConnection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(databaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public void createStatement(){
//        try {
//            statement = mySqlConnection.createStatement();
//        } catch (SQLException ex) {
//            Logger.getLogger(databaseHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        
//    }


    
}


