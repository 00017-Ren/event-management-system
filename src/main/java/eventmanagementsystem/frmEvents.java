/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eventmanagementsystem;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

/**
 *
 * @author Ren
 */
public class frmEvents extends javax.swing.JFrame {

    /**
     * Creates new form frmEvents
     */
    public frmEvents() {
        initComponents();
        // Populate dropdown lists on launch of window
        loadEvents();
        loadVenues();
    }
    
    // Create variables
    Boolean boolRecordExists=false;
    Boolean correctDateFormat=false;
    Boolean correctTimeFormat=false;
    Boolean DateTimeFormatIsGood=false;
    Boolean IsEventAttended=false;
    
    
    int id;
    String name = null;
    String date = null;
    String time = null;
    String description = null;
    String organiser = null;
    String venue=null;
    
    databaseHelper dbh = new databaseHelper();
    
    Statement statement = null;
    
    
    public void retrieveValues(){
        // Reads data from text fields and sets data to variables
        name = editTxtEventName.getText();
        date = editTxtEventDate.getText();
        time = editTxtEventTime.getText();
        description = editTxtEventDescr.getText();     
        organiser = editTxtEventOrganiser.getText();     
        venue = comboVenues.getSelectedItem().toString();
    }
    
    public void clearFields(){
        // Clears all text from fields to enable new text to be input
        editTxtEventName.setText("");
        editTxtEventDate.setText("");
        editTxtEventTime.setText("");
        editTxtEventDescr.setText("");
        editTxtEventOrganiser.setText("");
        comboVenues.setSelectedIndex(0);
    }
    
    public void clearVariables(){
        // Set variables back to default for following operations
        int id = 0;
        name = null;
        date = null;
        time = null;
        description = null;
        organiser = null;
        venue = null;
    }
    
    public void clearComboBox(){
        // Clear data from combobox list
        String[] comboList = {"Select a venue"};
        DefaultComboBoxModel model = new DefaultComboBoxModel(comboList);
        comboEvents.setModel(model);
    }
    
    private void validateEventName(){
        
        // Checks events table if record oof current variable exists
        // and sets boolean variable accordingly
        ResultSet rs = null;
        
        try {
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String query = "SELECT name FROM events WHERE name='" + name + "'";

            statement.execute(query);
            rs = statement.getResultSet();
            boolRecordExists = rs.next();
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
                
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null, "Connection string not closed " + e);
            }
        }
    }
    
    public void validateDateTime(){
        // Checks the format and values of the entered date and time to be valid
        // Date format yyyy-MM-dd
        // Time format hh:mm:ss
        correctDateFormat = date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
        correctTimeFormat = time.matches("^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
        
        if (correctDateFormat == false){
            JOptionPane.showMessageDialog(null, 
                        "Incorrect date entered.");
        } else if (correctTimeFormat == false){
            JOptionPane.showMessageDialog(null, 
                        "Incorrect time entered.");
        }       
        else {
            DateTimeFormatIsGood=true;
        }
        
    }
    
    public void createEvent(){
        
        // Initiates INSERT query to db to create new event row
        try {
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String queryInsert = "INSERT INTO events " + 
                    "(name, date, time, description, organiser, venue)" + 
                    " VALUES ('" + 
                    name + "', " + "'" +
                    date + "', " + "'" +
                    time + "', " + "'" +
                    description + "', " + "'" +
                    organiser + "', " + "'" +
                    venue + "') ";
            
           
            statement.executeUpdate(queryInsert);
            JOptionPane.showMessageDialog(null, 
                    "Event created successfully!");
            
                
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            finally {
                try {
                    statement.close();
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, 
                            "Connection string not closed " + e);
                }
        }
    }
    
    public void loadEvents(){

        // Selects all current entries from events table
        ResultSet rs = null;
        
        try {
            
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String querySelect = "Select id, name "
                    + "FROM events ORDER BY date ASC";
            
            statement.execute(querySelect);
            rs = statement.getResultSet();
            
            while(rs.next()){
                comboEvents.addItem(rs.getString(2));
            }

                    
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (Exception e){
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
                    + "FROM venues "
                    + "WHERE availability = 1 "
                    + "ORDER BY name ASC";
            
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
    
    public void readSelectedEvent(){

        // Set variables to the currently selected event in the combobox
        ResultSet rs = null;
        
        try {

            name = comboEvents.getItemAt(comboEvents.getSelectedIndex());
            
            getEventID();
            
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String querySelect = "Select name, date, time, description, "
                    + "organiser, venue "
                    + "FROM events WHERE id =" + id;
            
            statement.execute(querySelect);
            rs = statement.getResultSet();
            
            while(rs.next()){
            name=rs.getString(1);
            date=rs.getString(2);
            time=rs.getString(3);
            description=rs.getString(4);
            organiser=rs.getString(5);
            venue=rs.getString(6);
            }
           
        }
            
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void getEventID(){

        // Retrieves the event id of the current event 
        // from the events table to faciliatte further operations
        ResultSet rs = null;
        
        try {

            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String querySelect = "Select id "
                    + "FROM events WHERE name = '" + name + "'";
            
            statement.execute(querySelect);
            rs = statement.getResultSet();
            
            while(rs.next()){
                id = rs.getInt(1);
            }
           
        }
            
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void setEventToGUI(){
        // Set the contents of the variables to be displayed in text fields
        editTxtEventName.setText(name);
        editTxtEventDate.setText(date);
        editTxtEventTime.setText(time);
        editTxtEventDescr.setText(description);
        editTxtEventOrganiser.setText(organiser);
        comboVenues.setSelectedItem(venue);
    }
    
    public void checkEventAttendees(){
        
        // Checks the attendees table for any entries that are associated 
        // to the current event stored in variables
        ResultSet rs = null;
        
        try {
            getEventID();
            
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String query = "SELECT * FROM attendees " + 
                    "WHERE event_id = " + id;
            
            statement.execute(query);
            rs = statement.getResultSet();
            IsEventAttended = rs.next();
                 
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void updateEvent(){
        
        // Runs UPDATE query to amend the information
        // of an existing event in the events table
        try {
            
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String queryUpdate = "UPDATE events " + 
                    "SET name = '" + name + "', " +
                    "date = '" + date + "', " +
                    "time = '" + time + "', " +
                    "description = '" + description + "', " +
                    "organiser = '" + organiser + "', " + 
                    "venue = '" + venue + "' " + 
                    "WHERE id = " + id;
            
            statement.executeUpdate(queryUpdate);
            
            JOptionPane.showMessageDialog(
                        null, "Updated succesfully!");
                    
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try {
                statement.close();
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, 
                        "Connection string not closed " + e);
            }
        }
    }
    
    public void deleteEvent(){
        
        // Runs DELETE query to remove an event from the events table
        if (IsEventAttended == false){
            try {
            dbh.connectDb();
            statement = dbh.mySqlConnection.createStatement();
            
            String queryDelete = "DELETE FROM events " + 
                    "WHERE id = " + id;
            
            statement.executeUpdate(queryDelete);
                   
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            finally {
                try {
                    statement.close();
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, 
                            "Connection string not closed " + e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, 
                    "Cannot delete event with registered attendees.", 
                    "Event Attended", JOptionPane.WARNING_MESSAGE);
                        
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
        txtEvents = new javax.swing.JLabel();
        comboEvents = new javax.swing.JComboBox<>();
        pnlMid = new javax.swing.JPanel();
        editTxtEventDescr = new javax.swing.JTextField();
        editTxtEventTime = new javax.swing.JTextField();
        editTxtEventDate = new javax.swing.JTextField();
        editTxtEventName = new javax.swing.JTextField();
        txtEventDescr = new javax.swing.JLabel();
        txtEventTime = new javax.swing.JLabel();
        txtEventDate = new javax.swing.JLabel();
        txtEventName = new javax.swing.JLabel();
        txtEventOrganiser = new javax.swing.JLabel();
        editTxtEventOrganiser = new javax.swing.JTextField();
        comboVenues = new javax.swing.JComboBox<>();
        pnlBottom = new javax.swing.JPanel();
        btnEventAdd = new javax.swing.JButton();
        btnEventDelete = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(new java.awt.Color(100, 112, 143));
        setMaximumSize(new java.awt.Dimension(600, 500));
        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(600, 500));
        setResizable(false);
        setSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        pnlTop.setPreferredSize(new java.awt.Dimension(600, 100));
        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {0};
        jPanel1Layout.rowHeights = new int[] {0, 6, 0, 6, 0};
        pnlTop.setLayout(jPanel1Layout);

        txtEvents.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtEvents.setText("Events");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlTop.add(txtEvents, gridBagConstraints);

        comboEvents.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select event to view" }));
        comboEvents.setMaximumSize(new java.awt.Dimension(150, 30));
        comboEvents.setMinimumSize(new java.awt.Dimension(150, 30));
        comboEvents.setPreferredSize(new java.awt.Dimension(200, 30));
        comboEvents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEventsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        pnlTop.add(comboEvents, gridBagConstraints);

        getContentPane().add(pnlTop);

        pnlMid.setMaximumSize(new java.awt.Dimension(600, 250));
        pnlMid.setMinimumSize(new java.awt.Dimension(600, 250));
        pnlMid.setPreferredSize(new java.awt.Dimension(600, 250));
        java.awt.GridBagLayout jPanel2Layout = new java.awt.GridBagLayout();
        jPanel2Layout.columnWidths = new int[] {0, 5, 0, 5, 0};
        jPanel2Layout.rowHeights = new int[] {0, 6, 0, 6, 0, 6, 0, 6, 0, 6, 0, 6, 0};
        pnlMid.setLayout(jPanel2Layout);

        editTxtEventDescr.setColumns(30);
        editTxtEventDescr.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtEventDescr.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtEventDescr.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtEventDescr.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlMid.add(editTxtEventDescr, gridBagConstraints);

        editTxtEventTime.setColumns(30);
        editTxtEventTime.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        editTxtEventTime.setText("HH:MM:SS");
        editTxtEventTime.setToolTipText("HH:MM:SS");
        editTxtEventTime.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtEventTime.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtEventTime.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtEventTime.setPreferredSize(new java.awt.Dimension(350, 30));
        editTxtEventTime.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                editTxtEventTimeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                editTxtEventTimeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlMid.add(editTxtEventTime, gridBagConstraints);

        editTxtEventDate.setColumns(30);
        editTxtEventDate.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        editTxtEventDate.setText("YYYY-MM-DD");
        editTxtEventDate.setToolTipText("YYYY-MM-DD");
        editTxtEventDate.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtEventDate.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtEventDate.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtEventDate.setPreferredSize(new java.awt.Dimension(350, 30));
        editTxtEventDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                editTxtEventDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                editTxtEventDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlMid.add(editTxtEventDate, gridBagConstraints);

        editTxtEventName.setColumns(30);
        editTxtEventName.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtEventName.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtEventName.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtEventName.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlMid.add(editTxtEventName, gridBagConstraints);

        txtEventDescr.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEventDescr.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtEventDescr, gridBagConstraints);

        txtEventTime.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEventTime.setText("Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtEventTime, gridBagConstraints);

        txtEventDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEventDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtEventDate, gridBagConstraints);

        txtEventName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEventName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtEventName, gridBagConstraints);

        txtEventOrganiser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEventOrganiser.setText("Organiser");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMid.add(txtEventOrganiser, gridBagConstraints);

        editTxtEventOrganiser.setMargin(new java.awt.Insets(3, 6, 2, 6));
        editTxtEventOrganiser.setMaximumSize(new java.awt.Dimension(350, 30));
        editTxtEventOrganiser.setMinimumSize(new java.awt.Dimension(350, 30));
        editTxtEventOrganiser.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlMid.add(editTxtEventOrganiser, gridBagConstraints);

        comboVenues.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select a venue" }));
        comboVenues.setPreferredSize(new java.awt.Dimension(350, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        pnlMid.add(comboVenues, gridBagConstraints);

        getContentPane().add(pnlMid);

        pnlBottom.setMaximumSize(new java.awt.Dimension(600, 150));
        pnlBottom.setMinimumSize(new java.awt.Dimension(600, 150));
        pnlBottom.setPreferredSize(new java.awt.Dimension(600, 200));
        pnlBottom.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        btnEventAdd.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnEventAdd.setText("Add");
        btnEventAdd.setMaximumSize(new java.awt.Dimension(100, 30));
        btnEventAdd.setMinimumSize(new java.awt.Dimension(100, 30));
        btnEventAdd.setPreferredSize(new java.awt.Dimension(100, 30));
        btnEventAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEventAddActionPerformed(evt);
            }
        });
        pnlBottom.add(btnEventAdd);

        btnEventDelete.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnEventDelete.setText("Clear");
        btnEventDelete.setMaximumSize(new java.awt.Dimension(100, 30));
        btnEventDelete.setMinimumSize(new java.awt.Dimension(100, 30));
        btnEventDelete.setPreferredSize(new java.awt.Dimension(100, 30));
        btnEventDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEventDeleteActionPerformed(evt);
            }
        });
        pnlBottom.add(btnEventDelete);

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

    private void btnEventAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventAddActionPerformed
        
        // Checks the current label of button to perform the correct function
        if (null != btnEventAdd.getText())switch (btnEventAdd.getText()) {
            case "Add":
                retrieveValues();
                validateEventName();
                validateDateTime();
                
                // Checks if event name already exists before creating new event
                if(boolRecordExists==true){
                    boolRecordExists=false;
                    JOptionPane.showMessageDialog(null, "Event already exists");
                } else if ((boolRecordExists==false) && (DateTimeFormatIsGood==true)) {
                    DateTimeFormatIsGood=false;
                    createEvent();
                    clearComboBox();
                    clearFields();
                    clearVariables();
                    loadEvents();
                }   break;
            case "Edit":
                
                // Enables fields to edit existing event
                editTxtEventName.setEditable(true);
                editTxtEventDate.setEditable(true);
                editTxtEventTime.setEditable(true);
                editTxtEventDescr.setEditable(true);
                editTxtEventOrganiser.setEditable(true);
                comboVenues.setEnabled(true);
                btnEventAdd.setText("Save");
                btnEventDelete.setText("Cancel");
                break;
            case "Save":
                retrieveValues();
                validateDateTime();
                // Checks to see if date and time entered corectly from
                // Boolean variable
                if (DateTimeFormatIsGood==true){
                    DateTimeFormatIsGood=false;
                    updateEvent();
                    clearComboBox();
                    clearFields();
                    clearVariables();
                    loadEvents();
                    btnEventAdd.setText("Add");
                    btnEventDelete.setText("Clear");
                }
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnEventAddActionPerformed

    private void comboEventsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEventsActionPerformed
        readSelectedEvent();
        
        int index = comboEvents.getSelectedIndex();
        
        // When event is selected, disables editing untill edit button is clicked
        if (index > 0) {
            setEventToGUI();
            editTxtEventName.setEditable(false);
            editTxtEventDate.setEditable(false);
            editTxtEventTime.setEditable(false);
            editTxtEventDescr.setEditable(false);
            editTxtEventOrganiser.setEditable(false);
            comboVenues.setEnabled(false);
            btnEventAdd.setText("Edit");
            btnEventDelete.setText("Delete");
            
        // When default entry is selected, sets fields back to initial state     
        } else if (index == 0){
            clearFields();
            editTxtEventName.setEditable(true);
            editTxtEventDate.setEditable(true);
            editTxtEventTime.setEditable(true);
            editTxtEventDescr.setEditable(true);
            editTxtEventOrganiser.setEditable(true);
            comboVenues.setEnabled(true);
            btnEventAdd.setText("Add");
            btnEventDelete.setText("Clear");
        }
    }//GEN-LAST:event_comboEventsActionPerformed

    private void btnEventDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventDeleteActionPerformed
        if ("Clear".equals(btnEventDelete.getText())){
            clearFields();
        } else if ("Delete".equals(btnEventDelete.getText())){
            // Shows prompt for user to confirm deletion.
            // This prevents accidental removal of data.
            int choice = JOptionPane.showConfirmDialog(rootPane, 
                    "Are you sure you want to delete this event?", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
            if (choice == 0){
                retrieveValues();
                checkEventAttendees();
                deleteEvent();
                clearComboBox();
                clearFields();
                clearVariables();
                loadEvents();
                editTxtEventName.setEditable(true);
                editTxtEventDate.setEditable(true);
                editTxtEventTime.setEditable(true);
                editTxtEventDescr.setEditable(true);
                editTxtEventOrganiser.setEditable(true);
                comboVenues.setEnabled(true);
                btnEventAdd.setText("Add");
                btnEventDelete.setText("Clear");
            }
        // Sets fields back initial state   
        } else {
            clearFields();
            clearVariables();
            clearComboBox();
            loadEvents();
            btnEventAdd.setText("Add");
            btnEventDelete.setText("Clear");
        }
        
    }//GEN-LAST:event_btnEventDeleteActionPerformed

    private void editTxtEventDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_editTxtEventDateFocusGained
        // Sets hint text to show required format
        String txt = editTxtEventDate.getText();
        if ("YYYY-MM-DD".equals(txt)){
            editTxtEventDate.setText("");
        }
        
    }//GEN-LAST:event_editTxtEventDateFocusGained

    private void editTxtEventDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_editTxtEventDateFocusLost
        // Sets hint text to show required format
        String txt = editTxtEventDate.getText();
        if ("".equals(txt)){
            editTxtEventDate.setText("YYYY-MM-DD");
        }
    }//GEN-LAST:event_editTxtEventDateFocusLost

    private void editTxtEventTimeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_editTxtEventTimeFocusGained
        // Sets hint text to show required format
        String txt = editTxtEventTime.getText();
        if ("HH:MM:SS".equals(txt)){
            editTxtEventTime.setText("");
        }
    }//GEN-LAST:event_editTxtEventTimeFocusGained

    private void editTxtEventTimeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_editTxtEventTimeFocusLost
        // Sets hint text to show required format
        String txt = editTxtEventTime.getText();
        if ("".equals(txt)){
            editTxtEventTime.setText("HH:MM:SS");
        }
    }//GEN-LAST:event_editTxtEventTimeFocusLost

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
            java.util.logging.Logger.getLogger(frmEvents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmEvents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmEvents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmEvents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmEvents().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEventAdd;
    private javax.swing.JButton btnEventDelete;
    private javax.swing.JButton btnHome;
    private javax.swing.JComboBox<String> comboEvents;
    private javax.swing.JComboBox<String> comboVenues;
    private javax.swing.JTextField editTxtEventDate;
    private javax.swing.JTextField editTxtEventDescr;
    private javax.swing.JTextField editTxtEventName;
    private javax.swing.JTextField editTxtEventOrganiser;
    private javax.swing.JTextField editTxtEventTime;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlMid;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JLabel txtEventDate;
    private javax.swing.JLabel txtEventDescr;
    private javax.swing.JLabel txtEventName;
    private javax.swing.JLabel txtEventOrganiser;
    private javax.swing.JLabel txtEventTime;
    private javax.swing.JLabel txtEvents;
    // End of variables declaration//GEN-END:variables
}
