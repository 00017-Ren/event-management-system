/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eventmanagementsystem;


import java.awt.HeadlessException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Ren
 */
public class frmVenues extends javax.swing.JFrame {

    /**
     * Creates new form frmVenues
     */
    public frmVenues() {
        initComponents();
        // Populate dropdown list on launch of window
        loadVenues();
    }
    
    // Creates variables
    Boolean boolRecordExists=false;
    
    databaseHelper dbh = new databaseHelper();
    
    Statement statement = null;
    
    int id;
    String name=null;
    String address=null;
    String capacity=null;
    int availability=0;
    
    String[] comboListSelect = {"Select a venue"};
    
    public void retrieveValues(){
        
        // Reads data from text fields and sets data to variables
        name = editTxtVenName.getText();
        address = editTxtVenAddress.getText();
        capacity = editTxtVenCap.getText();
        
        if (radBtnYes.isSelected()){
           availability = 1; 
        } else availability = 0;
               
    }
    
    public void clearFields(){
        // Clears all text from fields to enable new text to be input
        editTxtVenName.setText("");
        editTxtVenAddress.setText("");
        editTxtVenCap.setText("");
        btnGrpYN.clearSelection();
    }
    
    public void clearVariables(){
        // Set variables back to default for following operations
        int id = 0;
        name = null;
        address = null;
        capacity = null;
        availability = 0;
    }
    
    public void clearComboBox(){
        // Clear data from combobox list
        DefaultComboBoxModel model = new DefaultComboBoxModel(comboListSelect);
        comboVenues.setModel(model);
    }
    
    public void validateVenue(){
        
        // Checks venues table if record of the current variables already exist
        // and sets boolean variable accordingly
        ResultSet rs = null;
        
        try {
            dbh.connectDb();
            
            statement = dbh.mySqlConnection.createStatement();
            
            String query = "SELECT * FROM venues WHERE name='" + name + 
                    "' AND address='" + address + 
                    "' AND capacity='" + capacity + 
                    "' AND availability='" + availability + "'";
            
            statement.execute(query);
            rs = statement.getResultSet();
            boolRecordExists = rs.next();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
                
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        null, "Connection string not closed " + e);
            }
        }
    }
    
    public void createVenue(){
        
        // Initiates INSERT query to db to create new venue row
        try {
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String queryInsert = "INSERT INTO venues " + 
                    "(name, address, capacity, availability)" + " VALUES ('" + 
                    name + "', " + "'" +
                    address + "', " + "'" +
                    capacity + "', " + "'" +
                    availability + "') ";
            
            statement.executeUpdate(queryInsert);
            
            JOptionPane.showMessageDialog(null, "Venue created successfully!");
            
                    
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void loadVenues(){
        // Selects all current entries from venues table

        ResultSet rs = null;
        
        try {
            
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String querySelect = "Select id, name "
                    + "FROM venues ORDER BY name ASC";
            
            statement.execute(querySelect);
            rs = statement.getResultSet();
            
            while(rs.next()){
                comboVenues.addItem(rs.getString(2));
            }                    
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
        
    }
    
    public void readSelectedVenue(){
        
        // Set variables to the currently selected event in the combobox
        ResultSet rs = null;
        
        try {

            name = comboVenues.getItemAt(comboVenues.getSelectedIndex());
            
            getVenueID();
            
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String querySelect = "Select name, address, capacity, availability "
                    + "FROM venues WHERE id =" + id;
            
            statement.execute(querySelect);
            rs = statement.getResultSet();
            
            while(rs.next()){
            name=rs.getString(1);
            address=rs.getString(2);
            capacity=rs.getString(3);
            availability=rs.getInt(4);
            }
           
        }
            
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void setVenueToGUI(){
        // Set the contents of the variables to be displayed in text fields
        editTxtVenName.setText(name);
        editTxtVenAddress.setText(address);
        editTxtVenCap.setText(capacity);
        if (availability == 1){
            radBtnYes.setSelected(true);
        } else radBtnNo.setSelected(true);
    }
    
    public void updateVenue(){
        
        // Runs UPDATE query to amend the information
        // of an existing venue in the venues table
        try {
            
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String queryUpdate = "UPDATE venues " + 
                    "SET name = '" + name + "', " +
                    "address = '" + address + "', " +
                    "capacity = '" + capacity + "', " +
                    "availability = '" + availability + "' " + 
                    "WHERE id = " + id;
            
            statement.executeUpdate(queryUpdate);
            
            JOptionPane.showMessageDialog(
                        null, "Venue updated succesfully!");
                    
        }
        catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void deleteVenue(){
        
        // Runs DELETE query to remove an event from the events table
        try {
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String queryDelete = "DELETE FROM venues " + 
                    "WHERE id = " + id;
            
            statement.executeUpdate(queryDelete);
            
            JOptionPane.showMessageDialog(
                        null, "Deleted succesfully!");
                    
        }
        catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void getVenueID(){
        
        // Retrieves the venue id of the current event 
        // from the venues table to faciliatte further operations
        ResultSet rs = null;
        
        try {

            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String querySelect = "Select id "
                    + "FROM venues WHERE name = '" + name + "'";
            
            statement.execute(querySelect);
            rs = statement.getResultSet();
            
            while(rs.next()){
                id = rs.getInt(1);
            }
           
        }
            
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (SQLException e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
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

        btnGrpYN = new javax.swing.ButtonGroup();
        pnlTop = new javax.swing.JPanel();
        txtEvents = new javax.swing.JLabel();
        comboVenues = new javax.swing.JComboBox<>();
        pnlMid = new javax.swing.JPanel();
        editTxtVenName = new javax.swing.JTextField();
        editTxtVenAddress = new javax.swing.JTextField();
        editTxtVenCap = new javax.swing.JTextField();
        txtVenName = new javax.swing.JLabel();
        txtVenAddress = new javax.swing.JLabel();
        txtVenCap = new javax.swing.JLabel();
        txtVenAvail = new javax.swing.JLabel();
        radBtnYes = new javax.swing.JRadioButton();
        radBtnNo = new javax.swing.JRadioButton();
        pnlBottom = new javax.swing.JPanel();
        btnVenAdd = new javax.swing.JButton();
        btnVenDelete = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(600, 500));
        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        pnlTop.setPreferredSize(new java.awt.Dimension(600, 100));
        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {0};
        jPanel1Layout.rowHeights = new int[] {0, 6, 0};
        pnlTop.setLayout(jPanel1Layout);

        txtEvents.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtEvents.setText("Venues");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlTop.add(txtEvents, gridBagConstraints);

        comboVenues.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select venue to view" }));
        comboVenues.setToolTipText("");
        comboVenues.setFocusCycleRoot(true);
        comboVenues.setMaximumSize(new java.awt.Dimension(150, 30));
        comboVenues.setMinimumSize(new java.awt.Dimension(150, 30));
        comboVenues.setNextFocusableComponent(editTxtVenName);
        comboVenues.setPreferredSize(new java.awt.Dimension(200, 30));
        comboVenues.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                comboVenuesFocusGained(evt);
            }
        });
        comboVenues.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                comboVenuesCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        comboVenues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboVenuesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        pnlTop.add(comboVenues, gridBagConstraints);

        getContentPane().add(pnlTop);

        pnlMid.setMaximumSize(new java.awt.Dimension(600, 250));
        pnlMid.setMinimumSize(new java.awt.Dimension(600, 250));
        pnlMid.setPreferredSize(new java.awt.Dimension(600, 250));
        java.awt.GridBagLayout jPanel2Layout = new java.awt.GridBagLayout();
        jPanel2Layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel2Layout.rowHeights = new int[] {0, 6, 0, 6, 0, 6, 0, 6, 0, 6, 0};
        pnlMid.setLayout(jPanel2Layout);

        editTxtVenName.setColumns(30);
        editTxtVenName.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtVenName.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtVenName.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtVenName.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMid.add(editTxtVenName, gridBagConstraints);

        editTxtVenAddress.setColumns(30);
        editTxtVenAddress.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtVenAddress.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtVenAddress.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtVenAddress.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMid.add(editTxtVenAddress, gridBagConstraints);

        editTxtVenCap.setColumns(30);
        editTxtVenCap.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtVenCap.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtVenCap.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtVenCap.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMid.add(editTxtVenCap, gridBagConstraints);

        txtVenName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtVenName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtVenName, gridBagConstraints);

        txtVenAddress.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtVenAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtVenAddress, gridBagConstraints);

        txtVenCap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtVenCap.setText("Capacity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtVenCap, gridBagConstraints);

        txtVenAvail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtVenAvail.setText("Availability");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtVenAvail, gridBagConstraints);

        btnGrpYN.add(radBtnYes);
        radBtnYes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        radBtnYes.setText("Yes");
        radBtnYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radBtnYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(radBtnYes, gridBagConstraints);

        btnGrpYN.add(radBtnNo);
        radBtnNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        radBtnNo.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        pnlMid.add(radBtnNo, gridBagConstraints);

        getContentPane().add(pnlMid);

        pnlBottom.setPreferredSize(new java.awt.Dimension(600, 100));
        pnlBottom.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        btnVenAdd.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnVenAdd.setText("Add");
        btnVenAdd.setPreferredSize(new java.awt.Dimension(100, 30));
        btnVenAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenAddActionPerformed(evt);
            }
        });
        pnlBottom.add(btnVenAdd);

        btnVenDelete.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnVenDelete.setText("Clear");
        btnVenDelete.setPreferredSize(new java.awt.Dimension(100, 30));
        btnVenDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenDeleteActionPerformed(evt);
            }
        });
        pnlBottom.add(btnVenDelete);

        btnHome.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnHome.setText("Home");
        btnHome.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        pnlBottom.add(btnHome);

        getContentPane().add(pnlBottom);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        frmHome home = new frmHome();
        home.setLocationRelativeTo(null);
        home.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnVenAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenAddActionPerformed
        
        // Checks the current label of button to perform the correct function
        if (null != btnVenAdd.getText())switch (btnVenAdd.getText()) {
            case "Add":
                retrieveValues();
                validateVenue();
                // Checks if venue name already exists before creating new venue
                if(boolRecordExists==true){
                    boolRecordExists=false;
                    JOptionPane.showMessageDialog(null, "Venue already exists");
                } else {
                    createVenue();
                    clearComboBox();
                    clearFields();
                    clearVariables();
                    loadVenues();
                    
                }   break;
            case "Edit":
                // Enables fields to edit existing event
                editTxtVenName.setEditable(true);
                editTxtVenAddress.setEditable(true);
                editTxtVenCap.setEditable(true);
                radBtnYes.setEnabled(true);
                radBtnNo.setEnabled(true);
                btnVenAdd.setText("Save");
                btnVenDelete.setText("Cancel");
                break;
            case "Save":
                retrieveValues();
                validateVenue();
                // Checks if venue name already exists before creating new venue
                if(boolRecordExists==true){
                    boolRecordExists=false;
                    JOptionPane.showMessageDialog(null, "Venue already exists");
                } else {
                    updateVenue();
                    clearComboBox();
                    clearFields();
                    clearVariables();
                    loadVenues();
                    btnVenAdd.setText("Add");
                    btnVenDelete.setText("Clear");
                }
                break;
            default:
                break;
        }
        
        
        
    }//GEN-LAST:event_btnVenAddActionPerformed

    
    private void comboVenuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboVenuesActionPerformed
        readSelectedVenue();
        
        int index = comboVenues.getSelectedIndex();
        
        // When venue is selected from lsit, disables editing untill edit button is clicked
        if (index > 0) {
            setVenueToGUI();
            editTxtVenName.setEditable(false);
            editTxtVenAddress.setEditable(false);
            editTxtVenCap.setEditable(false);
            radBtnYes.setEnabled(false);
            radBtnNo.setEnabled(false);
            //editTxtVenAvail.setEditable(false);
            btnVenAdd.setText("Edit");
            btnVenDelete.setText("Delete");
 
        // When default entry is selected, sets fields back to initial state
        } else if (index == 0){
            clearFields();
            editTxtVenName.setEditable(true);
            editTxtVenAddress.setEditable(true);
            editTxtVenCap.setEditable(true);
            radBtnYes.setEnabled(true);
            radBtnNo.setEnabled(true);
            btnVenAdd.setText("Add");
            btnVenDelete.setText("Clear");
        }
        
        
    }//GEN-LAST:event_comboVenuesActionPerformed

    private void comboVenuesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboVenuesFocusGained
  
    }//GEN-LAST:event_comboVenuesFocusGained

    private void comboVenuesCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_comboVenuesCaretPositionChanged

    }//GEN-LAST:event_comboVenuesCaretPositionChanged

    private void btnVenDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenDeleteActionPerformed
        if ("Clear".equals(btnVenDelete.getText())){
            clearFields();
        } else if ("Delete".equals(btnVenDelete.getText())){
            // Shows prompt for user to confirm deletion.
            // This prevents accidental removal of data.
            int choice = JOptionPane.showConfirmDialog(rootPane, 
                    "Are you sure you want to delete this venue?", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
            if(choice == 0){
                retrieveValues();
                deleteVenue();
                clearComboBox();
                clearFields();
                clearVariables();
                loadVenues();
                editTxtVenName.setEditable(true);
                editTxtVenAddress.setEditable(true);
                editTxtVenCap.setEditable(true);
                btnVenAdd.setText("Add");
                btnVenDelete.setText("Clear"); 
            }
            
        // Sets fields back initial state 
        } else {
            clearFields();
            clearVariables();
            clearComboBox();
            loadVenues();
            btnVenAdd.setText("Add");
            btnVenDelete.setText("Clear");
        }   
    }//GEN-LAST:event_btnVenDeleteActionPerformed

    private void radBtnYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radBtnYesActionPerformed
        
    }//GEN-LAST:event_radBtnYesActionPerformed

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
            java.util.logging.Logger.getLogger(frmVenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmVenues().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGrpYN;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnVenAdd;
    private javax.swing.JButton btnVenDelete;
    private javax.swing.JComboBox<String> comboVenues;
    private javax.swing.JTextField editTxtVenAddress;
    private javax.swing.JTextField editTxtVenCap;
    private javax.swing.JTextField editTxtVenName;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlMid;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JRadioButton radBtnNo;
    private javax.swing.JRadioButton radBtnYes;
    private javax.swing.JLabel txtEvents;
    private javax.swing.JLabel txtVenAddress;
    private javax.swing.JLabel txtVenAvail;
    private javax.swing.JLabel txtVenCap;
    private javax.swing.JLabel txtVenName;
    // End of variables declaration//GEN-END:variables
}
