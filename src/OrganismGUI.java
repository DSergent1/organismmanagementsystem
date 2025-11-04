import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class OrganismGUI extends JFrame{
    private JPanel MainPanel;
    private JButton customMethodButton;
    private JButton addOrganismButton;
    private JTable JTOrganismTable;
    private JButton deleteOrganismButton;
    private JButton fileUploadButton;
    private JButton updateOrganismButton;
    private JScrollPane scrollPane;


    OrganismManager manager = new OrganismManager();

    //constructor for my GUI class with title, close, size, and other features
    public OrganismGUI (){
        setContentPane(MainPanel);
        setTitle("Organism Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300,600);
        setLocationRelativeTo(null);
        setVisible(true);

        /*                            file upload button with file explorer added                               */
        fileUploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Organism Data File (must have format id-clade-genus-lifespan-lifespan unit-features-average length-length unit): ");

            //a filter to ensure only txt files are chosen
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files","txt");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showOpenDialog(null);
            if(userSelection == JFileChooser.APPROVE_OPTION) {

                File fileChosen = fileChooser.getSelectedFile();
                String path = fileChosen.getAbsolutePath();

                String notification = manager.loadFile(path);
                JOptionPane.showMessageDialog(null, notification);

                OrganismTable();
            }

        });


        /*                        action listener for deleting organism by its id                         */
        deleteOrganismButton.addActionListener(e -> {
            String id = getSelectedOrganismRow();

            //check to ensure id is not null, isn't empty, is 5 digits, and is all digits
            if(id == null|| id.trim().isEmpty()|| id.length() != 5 || !id.chars().allMatch(Character::isDigit)){
                JOptionPane.showMessageDialog(null, "Incorrect id format");
            }

            int confirmDeletion = JOptionPane.showConfirmDialog(null, "Do you want to delete organism: " + id + "?",
                    "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmDeletion != JOptionPane.YES_OPTION) return;

            Organism removedOrganism = manager.removeOrganism(id);
            //after entering organism id, if-statement checks if id has a value
            if (removedOrganism != null){
                JOptionPane.showMessageDialog(null, "Organism: "+ id + " deleted");
                OrganismTable();
            }
            else {
                JOptionPane.showMessageDialog(null, "Not an existing organism.");
            }
        });


        /*                            add organism button with each individual attribute having an input box       */
        addOrganismButton.addActionListener(e -> {
            //needs to follow id format rules
            String id;
            while (true) {
                id = JOptionPane.showInputDialog("Enter ID (5 digits):");
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
            while(true) {
                 lengthUnit = JOptionPane.showInputDialog("Enter length unit (mm/cm/m): ");
                if (!lengthUnit.equals("mm") && !lengthUnit.equals("m") && !lengthUnit.equals("cm")) {
                    JOptionPane.showMessageDialog(null, "Length needs to be in mm, cm, or m.");
                    continue;
                }
                break;
            }

            boolean addedOrganism = manager.addOrganism(new Organism(id, clade, genus, lifespan,
                    lifespanUnit, features, avgLength, lengthUnit));
            JOptionPane.showMessageDialog(null, addedOrganism ? "Organism added." : "Failed to add the organism.");
            OrganismTable();
        });


        /*              update button action listener with combo box to select attribute                */
        updateOrganismButton.addActionListener(e ->{
            String id = getSelectedOrganismRow();
            //check to ensure id is not null, isn't empty, is 5 digits, and is all digits
            if(id == null|| id.trim().isEmpty()|| id.length() != 5 || !id.chars().allMatch(Character::isDigit)){
                JOptionPane.showMessageDialog(null, "Incorrect id format");
                return;
            }
            Organism exists = null;
            for (Organism organism : manager.getOrganisms()) {
                if (organism.getId().equals(id)) {
                    exists = organism;
                    break;
                }
            }
            if (exists == null) {
                JOptionPane.showMessageDialog(null, "No organism found with ID: " + id);
                return;
            }

            //making the combo box to select attribute to update in organism
            String[] attribute = {"clade", "genus", "lifespan", "lifespan unit", "features", "average length", "lengthunit"};
            String cBoxChoice = (String) JOptionPane.showInputDialog(null, "Select the attribute to update:",
                    "Update Organism", JOptionPane.QUESTION_MESSAGE, null, attribute, attribute[0]);

            if (cBoxChoice == null) {
                return;
            }

            //asking for updated value and making sure value entered isn't null
            String newValue = JOptionPane.showInputDialog("Enter new value of updated attribute (" + cBoxChoice+ "): ");
            if (newValue == null){
                JOptionPane.showMessageDialog(null, "Please enter a value: ");
                return;
            }

            // add in all parameters to update and check if update was successful
            boolean updatedOrganism = manager.updateOrganism(id, cBoxChoice, newValue);

            if (updatedOrganism){
                JOptionPane.showMessageDialog(null, "Updated " + cBoxChoice + " to " + newValue);
                OrganismTable();
            }
            else {
                JOptionPane.showMessageDialog(null, "Failed to update organism.");
            }
        });


        /*                         custom method button gui to display average lengths by their clades               */
        customMethodButton.addActionListener(e ->{
            ArrayList<String> enteredClades = new ArrayList<>();
            StringBuilder outputCladeAverages = new StringBuilder();

            for (Organism organism : manager.getOrganisms()){
                String clade = organism.getCladeName();
                if(enteredClades.contains(clade)) continue;

                float avg = manager.getAvg(clade);
                outputCladeAverages.append(String.format("Clade: %s | Average Length: %.2f cm%n ", clade, avg));
                enteredClades.add(clade);
            }
            JOptionPane.showMessageDialog(null, outputCladeAverages.toString());
                });
    }

    public static void main(String[] args) {
        new OrganismGUI();

    }

    //allows selecting a row to get ID for deleting or updating
    private String getSelectedOrganismRow(){
        int selectedRow = JTOrganismTable.getSelectedRow();
        //ensures a row is selected when clicking the button
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(null, "Please select an organism from the table.");
            return null;
        }
        //gets value of ID at column 0
        return JTOrganismTable.getValueAt(selectedRow,0).toString();
    }

    //creating a table for the GUI, method used to update table after changes
    private void OrganismTable(){
        String [] attributes = {"ID", "Clade", "Genus & Species", "Lifespan", "Lifespan Unit",
                "Features", "Average Length", "Length unit"};
        DefaultTableModel OrganismModel = new DefaultTableModel(attributes, 0);
//adding each attribute of the organism to the table
        for(Organism organism  : manager.getOrganisms()){
            Object[] OrganismRow = { organism.getId(), organism.getCladeName(), organism.getGenusSpecies(), organism.getLifespanEstimate(),
                    organism.getLifespanUnit(), organism.getDefinitiveFeatures(), organism.getAverageLength(), organism.getLengthUnit() };
            OrganismModel.addRow(OrganismRow);
        }
        JTOrganismTable.setModel(OrganismModel);

        //setting column widths as attributes like features need more room
        int [] columnWidths = {80, 150, 300, 60, 80, 400, 100, 80};
        for (int widthNumber = 0; widthNumber < columnWidths.length; widthNumber++){
            JTOrganismTable.getColumnModel().getColumn(widthNumber).setPreferredWidth(columnWidths[widthNumber]);
        }
        JTOrganismTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
}


