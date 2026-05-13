/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package eventmanagementsystem;

import com.formdev.flatlaf.FlatLightLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ren
 */
public class EventManagementSystem {
    


    /**
     * @param args the command line arguments
     */
    static void main(String[] args) {
        
        FlatLightLaf.setup();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(EventManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
        }

        databaseHelper dbh = new databaseHelper();
        dbh.initializeSchema();

        frmHome home = new frmHome();
        home.setLocationRelativeTo(null);
        home.setVisible(true);
        
        
        
    }
    
    
    
}
