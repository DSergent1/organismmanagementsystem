

package com.organism;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;

/**
 * GUI class for the Organism Application
 *
 * Phase 3
 * Organism GUI file to create an interface that implements functions from the previous phases.
 *
 * Phase 4 -Adding database functionality to the buttons created in the GUI form
 * Allows users to select a db file to use, a txt file to upload some organisms, CRUD operations by GUI, and custom method
 * all added to the database.
 */
public class OrganismGUI extends JFrame {
    private JPanel MainPanel;
    private JButton customMethodButton;
    private JButton addOrganismButton;
    private JTable JTOrganismTable;
    private JButton deleteOrganismButton;
    private JButton fileUploadButton;
    private JButton updateOrganismButton;
    private DatabaseHelper dbHelper;


    /**
     * Creates new Organism Manager
     */
    OrganismManager manager = new OrganismManager();
    /**
     * Constructor for the Organism GUI window, adds all the interface components,
     * has the user select a database file to start, and loads all organisms from it.
     *
     * This constructor also has button listeners for: file uploading from txt, adding an organism form,
     * deleting an organism by selecting it on the table, updating attributes by selecting it on the table,
     * and a display for the custom method.
     */
    public OrganismGUI() {
        setContentPane(MainPanel);
        setTitle("Organism Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        //Here we set up adding a database to the application by user selection
        JFileChooser dbFileChooser = new JFileChooser();
        dbFileChooser.setDialogTitle("Select the Organisms Database File (.db)");
        dbFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        dbFileChooser.setFileFilter(new FileNameExtensionFilter("db files", "db"));

        int result = dbFileChooser.showOpenDialog(this);

        //if result is valid, continue with connection to database
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = dbFileChooser.getSelectedFile();
            String dbPath = selectedFile.getAbsolutePath();
            dbHelper = new DatabaseHelper(dbPath);
            if (dbHelper.testConnection()) {
                JOptionPane.showMessageDialog(null, "Database connected successfully!");
                // Load the table from the db data
                JTOrganismTable.setModel(dbHelper.getAllOrganisms());
            }
            //message if database could not be connected to
            else {
                JOptionPane.showMessageDialog(this,
                        "Could not connect to the selected database.");
                System.exit(0);
            }
        }
        //message for none selected
        else {
            JOptionPane.showMessageDialog(this,
                    "No database file selected. Exiting.");
            System.exit(0);
        }
        setVisible(true);


        /**
         * Handles uploading organism data from a .txt file, each valid line is checked and added into the database,
         * and the table is refreshed after the upload is done.
         */
        fileUploadButton.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Organism Data File (.txt)");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt")); // this keeps files strictly .txt

            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            File file = fileChooser.getSelectedFile();

            //added to show how many organisms were added
            int addedCount = 0;

            try (java.util.Scanner scanner = new java.util.Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split("-"); // splitting at the - delimiter
                    if (parts.length != 8) {
                        continue;
                    }
                    // takes in each part, checks for positive numbers and the length unit being acceptable
                    String id = parts[0];
                    String clade = parts[1];
                    String genus = parts[2];
                    int lifespan;
                    try {
                        lifespan = Integer.parseInt(parts[3]);
                        if (lifespan <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        continue;
                    }
                    String lifespanUnit = parts[4];
                    String features = parts[5];

                    float avgLength;
                    try {
                        avgLength = Float.parseFloat(parts[6]);
                        if (avgLength <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        continue;
                    }

                    String lengthUnit = parts[7];
                    if (!lengthUnit.equals("mm") && !lengthUnit.equals("cm") && !lengthUnit.equals("m")) {
                        continue;
                    }
                    // Add to the database via dbHelper
                    dbHelper.addOrganism(id, clade, genus, lifespan, lifespanUnit, features, avgLength, lengthUnit);
                    addedCount++;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading file:\n" + ex.getMessage());
            }

            JOptionPane.showMessageDialog(this, "File upload finished.\nAdded: " + addedCount);
            JTOrganismTable.setModel(dbHelper.getAllOrganisms()); //updates the GUI table
        });

        /**
         * Delete button for the organism that is selected, asks for confirmation, then refreshes the table after.
         */
        deleteOrganismButton.addActionListener(e -> {
            String id = getSelectedOrganismRow();

            //check to ensure id is not null, isn't empty, is 5 digits, and is all digits
            if (id == null || id.trim().isEmpty() || id.length() != 5 || !id.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(null, "Incorrect id format");
            }

            int confirmDeletion = JOptionPane.showConfirmDialog(null, "Do you want to delete organism: " + id + "?",
                    "Delete Confirmation", JOptionPane.YES_NO_OPTION); //option to select yes or no on deletion
            if (confirmDeletion != JOptionPane.YES_OPTION) return;

            boolean deleted = dbHelper.deleteOrganism(id);
            //after entering organism id, if-statement checks if id has a value
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Organism: " + id + " deleted");
                JTOrganismTable.setModel(dbHelper.getAllOrganisms()); //updates table after deleting
            } else {
                JOptionPane.showMessageDialog(null, "Not an existing organism.");
            }
        });


        /**
         * Opens up a series of input boxes to add organism information one attribute at a time and validates each
         * attribute before moving on to the next.
         */
        addOrganismButton.addActionListener(e -> {
            //needs to follow id format rules
            String id;
            while (true) {
                id = JOptionPane.showInputDialog("Enter ID (5 digits):");
                //check if the id entered follows the rules and isn't a duplicate
                boolean validId = id.length() == 5 && id.chars().allMatch(Character::isDigit);
                if (!validId) {
                    JOptionPane.showMessageDialog(null, "ID must be exactly 5 digits.");
                    continue;
                }
                if (manager.idDuplicate(id)) {
                    JOptionPane.showMessageDialog(null, "ID already exists. Enter a unique ID.");
                    continue;
                }
                break;
            }

            String clade = JOptionPane.showInputDialog("Enter clade: ");
            String genus = JOptionPane.showInputDialog("Enter Genus & Species: ");
            //lifespan needs to be a positive integer
            int lifespan;
            while (true) {
                String lifespanStr = JOptionPane.showInputDialog("Enter lifespan (positive integer):");
                try {
                    lifespan = Integer.parseInt(lifespanStr);
                    if (lifespan <= 0) {
                        JOptionPane.showMessageDialog(null, "Lifespan must be positive.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lifespan must be an integer.");
                }
            }
            ;

            String lifespanUnit = JOptionPane.showInputDialog("Enter lifespan unit: ");
            String features = JOptionPane.showInputDialog("Enter definitive features");

            //Ensure average length is positive, and is a float
            float avgLength;
            while (true) {
                String avgStr = JOptionPane.showInputDialog("Enter average length (positive number):");
                if (avgStr == null) return;
                try {
                    avgLength = Float.parseFloat(avgStr);
                    if (avgLength <= 0) {
                        JOptionPane.showMessageDialog(null, "Length needs to be positive.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Length needs to be a number.");
                }
            }
            ;
            //length unit needs to be in m, mm, or cm
            String lengthUnit;
            while (true) {
                lengthUnit = JOptionPane.showInputDialog("Enter length unit (mm/cm/m): ");
                if (!lengthUnit.equals("mm") && !lengthUnit.equals("m") && !lengthUnit.equals("cm")) {
                    JOptionPane.showMessageDialog(null, "Length needs to be in mm, cm, or m.");
                    continue;
                }
                break;
            }

            boolean addedOrganism = dbHelper.addOrganism(id, clade, genus, lifespan, lifespanUnit, features, avgLength, lengthUnit);
            //checks if organism was added and sends appropriate message to user
            JOptionPane.showMessageDialog(null, addedOrganism ? "Organism added." : "Failed to add the organism.");
            JTOrganismTable.setModel(dbHelper.getAllOrganisms());
        });


        /**
         * Allow the user to select an attribute and update its value for the selected organism.
         * Refreshes the table after successful update.
         */
        updateOrganismButton.addActionListener(e -> {
            String id = getSelectedOrganismRow();
            if (id == null) return;

            //creates an array for the combo box
            String[] attribute = {"clade", "genus", "lifespan", "lifespan unit", "features", "average length", "lengthunit"};
            String cBoxChoice = (String) JOptionPane.showInputDialog(null,
                    "Select the attribute to update:", "Update Organism", JOptionPane.QUESTION_MESSAGE,
                    null, attribute, attribute[0]);
            if (cBoxChoice == null) return; //returns if nothing was chosen

            String newValue = JOptionPane.showInputDialog("Enter new value for " + cBoxChoice + ":");
            if (newValue == null || newValue.isEmpty()) return; //check to see if new value is empty or null

            boolean updated = dbHelper.updateOrganism(id, cBoxChoice, newValue);

            if (updated) {
                JOptionPane.showMessageDialog(null, "Organism updated successfully.");
                JTOrganismTable.setModel(dbHelper.getAllOrganisms());
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update organism.");
            }
        });

        /**
         * Displays the calculated average lengths of organisms grouped by their clades.
         * It then shows the formatted results.
         */
        customMethodButton.addActionListener(e -> {
            String averagesOutput = dbHelper.getAverageLengthByCladeString();
            //checks to see if database is empty before displaying custom method
            if (averagesOutput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No organisms in the database.");
                return;
            }
            JOptionPane.showMessageDialog(this, averagesOutput, "Average Lengths by Clade", JOptionPane.INFORMATION_MESSAGE);
        });

    }

    /**
     * Starts the program and constructs the new GUI
     * @param args
     */
    public static void main(String[] args) {
        new OrganismGUI();

    }

    /**
     * Returns the ID of the organism in the selected table row.
     *
     * If no row is selected, a message is shown to the user and null is returned.
     *
     * @return the 5-digit organism ID from column 0, or null if no row is selected
     */
    private String getSelectedOrganismRow() {
        int selectedRow = JTOrganismTable.getSelectedRow();
        //ensures a row is selected when clicking the button
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an organism from the table.");
            return null;
        }
        //gets value of ID at column 0
        return JTOrganismTable.getValueAt(selectedRow, 0).toString();
    }
}




