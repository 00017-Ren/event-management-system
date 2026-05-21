/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eventmanagementsystem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

/**
 *
 * @author Ren
 */
public class frmAttendees extends javax.swing.JFrame {

    /**
     * Creates new form frmAttendees
     */
    public frmAttendees() {
        initComponents();
        // Populate dropdown list on launch of window
        loadEvents();
    }
    
    //Create variables
    Boolean boolRecordExists=false;
    Boolean correctEmailFormat=false;
    Boolean correctPhoneFormat=false;
    Boolean emailPhoneFormatIsGood=false;
    
    databaseHelper dbh = new databaseHelper();
    
    Statement statement = null;
    
    int id;
    String name=null;
    String email=null;
    String number=null;
    int event_id=0;
    String event_name=null;
    
    public void retrieveValues(){
        // Reads data from text fields and sets data to variables
        name = editTxtAttName.getText();
        email = editTxtAttEmail.getText();
        number = editTxtAttNumber.getText();
        event_name = editTxtAttEvent.getText();       
    }
    
    public void clearFields(){
        // Clears all text from fields to enable new text to be input
        editTxtAttName.setText("");
        editTxtAttEmail.setText("");
        editTxtAttNumber.setText("");  
        editTxtAttEvent.setText("");
    }
    
    public void clearVariables(){
        // Set variables back to default for following operations
        int id = 0;
        name = null;
        email = null;
        number = null;
        event_id=0;
        event_name=null;
    }
    
    public void clearComboBox(){
        // Clear data from combobox list
        String[] comboList = {"Select an event"};
        DefaultComboBoxModel model = new DefaultComboBoxModel(comboList);
        comboEvents.setModel(model);
    }
    
    public void loadEvents(){
        dbh.connectDb();
        // Selects all current entries from events table
        String querySelect = "Select id, name FROM events ORDER BY date ASC";

        try (PreparedStatement pstmt = dbh.mySqlConnection.prepareStatement(querySelect)) {
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()){
                comboEvents.addItem(rs.getString(2));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void registerAttendee(){
        dbh.connectDb();
        // Runs INSERT query to create new entry in attendee table
        String queryInsert = "INSERT INTO attendees (name, email, contact, event_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbh.mySqlConnection.prepareStatement(queryInsert)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, number);
            pstmt.setInt(4, event_id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void validateRegistration(){
        dbh.connectDb();
        // Checks attendees table if record of current variables exists
        // and sets boolean variable accordingly
        ResultSet rs = null;

        String query = "SELECT * FROM attendees WHERE name = ? AND email = ? AND contact = ? AND event_id = ?";
        
        try (PreparedStatement pstmt = dbh.mySqlConnection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, number);
            pstmt.setInt(4, event_id);
            rs = pstmt.executeQuery();

            boolRecordExists = rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void validateEmailPhone(){
        
        // Checks format of data enetered for email and phone number to ber valid
        // Email format x@x.com
        // Phone format is 10 digits
        correctEmailFormat = email.matches("^[_A-Za-z0-9-\\+]+"
                + "(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*"
                + "(\\.[A-Za-z]{2,})$");
        correctPhoneFormat = number.matches("\\d{10}");
        
        if (correctEmailFormat == false){
            JOptionPane.showMessageDialog(null, 
                        "Incorrect email entered.");
        } else if (correctPhoneFormat == false){
            JOptionPane.showMessageDialog(null, 
                        "Incorrect phone number entered.");
        }       
        else {
            emailPhoneFormatIsGood=true;
        }
    }
    
    public void getEventID(){
        dbh.connectDb();
        // Retrieves the event id of the current event 
        // from the events table to facilitate further operations
        ResultSet rs = null;

        String querySelect = "Select id FROM events WHERE name = ?";
        
        try (PreparedStatement pstmt = dbh.mySqlConnection.prepareStatement(querySelect)) {
            pstmt.setString(1, event_name);
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                event_id = rs.getInt(1);
            }
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void readSelectedEvent(){
        dbh.connectDb();
        // Set event variable to the currently selected event in the combobox
        // Set variable data to text field
        String querySelect = "Select name FROM events WHERE id = ?";
        
        try (PreparedStatement pstmt = dbh.mySqlConnection.prepareStatement(querySelect)) {
            event_name = comboEvents.getSelectedItem().toString();
            
            getEventID();
            
            pstmt.setInt(1, event_id);

            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()){
                editTxtAttEvent.setText(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTop = new javax.swing.JPanel();
        txtAttendees = new javax.swing.JLabel();
        comboEvents = new javax.swing.JComboBox<>();
        pnlMid = new javax.swing.JPanel();
        txtAttName = new javax.swing.JLabel();
        editTxtAttName = new javax.swing.JTextField();
        txtAttEmail = new javax.swing.JLabel();
        editTxtAttEmail = new javax.swing.JTextField();
        txtAttNumber = new javax.swing.JLabel();
        editTxtAttNumber = new javax.swing.JTextField();
        txtAttEvent = new javax.swing.JLabel();
        editTxtAttEvent = new javax.swing.JTextField();
        pnlBottom = new javax.swing.JPanel();
        btnAttendeeRedister = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(600, 500));
        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        pnlTop.setMaximumSize(new java.awt.Dimension(600, 100));
        pnlTop.setMinimumSize(new java.awt.Dimension(600, 100));
        pnlTop.setPreferredSize(new java.awt.Dimension(600, 100));
        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {0, 5, 0};
        jPanel1Layout.rowHeights = new int[] {0, 6, 0};
        pnlTop.setLayout(jPanel1Layout);

        txtAttendees.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtAttendees.setText("Attendees");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlTop.add(txtAttendees, gridBagConstraints);

        comboEvents.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboEvents.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select your event" }));
        comboEvents.setMaximumSize(new java.awt.Dimension(150, 30));
        comboEvents.setMinimumSize(new java.awt.Dimension(150, 30));
        comboEvents.setOpaque(true);
        comboEvents.setPreferredSize(new java.awt.Dimension(200, 30));
        comboEvents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEventsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        pnlTop.add(comboEvents, gridBagConstraints);

        getContentPane().add(pnlTop);

        pnlMid.setMaximumSize(new java.awt.Dimension(600, 250));
        pnlMid.setMinimumSize(new java.awt.Dimension(600, 250));
        pnlMid.setPreferredSize(new java.awt.Dimension(600, 250));
        java.awt.GridBagLayout jPanel2Layout = new java.awt.GridBagLayout();
        jPanel2Layout.columnWidths = new int[] {0, 5, 0};
        jPanel2Layout.rowHeights = new int[] {0, 6, 0, 6, 0, 6, 0, 6, 0};
        pnlMid.setLayout(jPanel2Layout);

        txtAttName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAttName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtAttName, gridBagConstraints);

        editTxtAttName.setColumns(30);
        editTxtAttName.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtAttName.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtAttName.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtAttName.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        pnlMid.add(editTxtAttName, gridBagConstraints);

        txtAttEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAttEmail.setText("Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtAttEmail, gridBagConstraints);

        editTxtAttEmail.setColumns(30);
        editTxtAttEmail.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtAttEmail.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtAttEmail.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtAttEmail.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        pnlMid.add(editTxtAttEmail, gridBagConstraints);

        txtAttNumber.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAttNumber.setText("Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtAttNumber, gridBagConstraints);

        editTxtAttNumber.setColumns(30);
        editTxtAttNumber.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtAttNumber.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtAttNumber.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtAttNumber.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        pnlMid.add(editTxtAttNumber, gridBagConstraints);

        txtAttEvent.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAttEvent.setText("Event");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtAttEvent, gridBagConstraints);

        editTxtAttEvent.setEditable(false);
        editTxtAttEvent.setColumns(30);
        editTxtAttEvent.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        editTxtAttEvent.setForeground(new java.awt.Color(100, 112, 143));
        editTxtAttEvent.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtAttEvent.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtAttEvent.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtAttEvent.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        pnlMid.add(editTxtAttEvent, gridBagConstraints);

        getContentPane().add(pnlMid);

        pnlBottom.setMaximumSize(new java.awt.Dimension(600, 100));
        pnlBottom.setMinimumSize(new java.awt.Dimension(600, 100));
        pnlBottom.setPreferredSize(new java.awt.Dimension(600, 100));
        java.awt.GridBagLayout jPanel3Layout = new java.awt.GridBagLayout();
        jPanel3Layout.columnWidths = new int[] {0, 5, 0, 5, 0};
        jPanel3Layout.rowHeights = new int[] {0, 6, 0, 6, 0, 6, 0, 6, 0, 6, 0, 6, 0, 6, 0};
        pnlBottom.setLayout(jPanel3Layout);

        btnAttendeeRedister.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnAttendeeRedister.setText("Register");
        btnAttendeeRedister.setMaximumSize(new java.awt.Dimension(100, 30));
        btnAttendeeRedister.setMinimumSize(new java.awt.Dimension(100, 30));
        btnAttendeeRedister.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAttendeeRedister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttendeeRedisterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlBottom.add(btnAttendeeRedister, gridBagConstraints);

        btnHome.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnHome.setText("Home");
        btnHome.setMaximumSize(new java.awt.Dimension(100, 30));
        btnHome.setMinimumSize(new java.awt.Dimension(100, 30));
        btnHome.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        pnlBottom.add(btnHome, gridBagConstraints);

        getContentPane().add(pnlBottom);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        frmHome home = new frmHome();
        home.setLocationRelativeTo(null);
        home.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnAttendeeRedisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttendeeRedisterActionPerformed
        retrieveValues();
        validateRegistration();
        validateEmailPhone();
        if (boolRecordExists == true){
            Boolean boolRecordExists=false;
            JOptionPane.showMessageDialog(null, 
                    "You have already registered for this event.");
        } else if (boolRecordExists == false && emailPhoneFormatIsGood == true) {
            emailPhoneFormatIsGood = false;
            System.out.println(event_id);
            System.out.println(event_name);
            registerAttendee();
            clearComboBox();
            clearFields();
            clearVariables();
            loadEvents();
            
        }
        
        
    }//GEN-LAST:event_btnAttendeeRedisterActionPerformed

    private void comboEventsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEventsActionPerformed
        readSelectedEvent();
        int index = comboEvents.getSelectedIndex();
        if (index > 0) {
            readSelectedEvent();
        } else if (index == 0) {
            clearFields();
        }
    }//GEN-LAST:event_comboEventsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmAttendees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAttendees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAttendees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAttendees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAttendees().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAttendeeRedister;
    private javax.swing.JButton btnHome;
    private javax.swing.JComboBox<String> comboEvents;
    private javax.swing.JTextField editTxtAttEmail;
    private javax.swing.JTextField editTxtAttEvent;
    private javax.swing.JTextField editTxtAttName;
    private javax.swing.JTextField editTxtAttNumber;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlMid;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JLabel txtAttEmail;
    private javax.swing.JLabel txtAttEvent;
    private javax.swing.JLabel txtAttName;
    private javax.swing.JLabel txtAttNumber;
    private javax.swing.JLabel txtAttendees;
    // End of variables declaration//GEN-END:variables
}
