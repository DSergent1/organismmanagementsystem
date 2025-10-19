/**
 *Name: Daniel Sergent
 *Due Date: 10/13/25
 *Course: CEN-3024C-14877
 *Professor: Dr. Macon

This Organism application will serve as a study aid for my Biology 2 course and data management
 system project for my Software Development 1 course.

This OrganismApplication class will handle the interface in CLI form in phase 1,
 and GUI via Java Swing in phase 3. Here, inputs from the user will be entered through a menu
 and CRUD actions plus a custom method(finding average organism size by their clade)
 can be chosen.
*/
import java.util.Scanner;


public class OrganismApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static OrganismManager manage = new OrganismManager();

    public static void main(String[] args) {

        boolean runProgram = true;
        while (runProgram) {
            displayMenu();

            System.out.print("Enter choice: ");
            String userChoice = scanner.nextLine().trim();

            //switch case to connected to methods either in OrganismManager or CLI methods below
            switch (userChoice) {
                case "1":
                    importOrganisms();
                    break;
                case "2":
                    addOrganismCLI();
                    break;
                case "3":
                    removeOrganismCLI();
                    break;
                case "4":
                    manage.displayOrganisms();
                    break;

                    case "5":
                    updateOrganismCLI();
                    break;
                case "6":
                    manage.displayAverageLengthByClade();
                    break;
                case "7":
                {
                    runProgram = false;
                    System.out.println("Exiting Organism Management System. Goodbye!");
                }
                break;

                    default:
                    System.out.println("Invalid choice. Please enter a number 1–7.");
            }
        }
    }

    // Menu for organism management
    public static void displayMenu() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("-----------------Organism Management System----------------");
        System.out.println("----1) Load organisms from file");
        System.out.println("----2) Add a new Organism");
        System.out.println("----3) Remove an Organism");
        System.out.println("----4) Display all Organisms");
        System.out.println("----5) Update an organism");
        System.out.println("----6) Display average length by clade");
        System.out.println("----7) Exit");
        System.out.println("-----------------------------------------------------------");
    }

    // Load organism data from file following the format given
    public static void importOrganisms() {
        System.out.print("Enter file path (format: ID-CladeName-GenusSpecies-LifespanEstimate-LifespanUnit-DefiniteFeatures-AverageLength-LengthUnit): ");
        String path = scanner.nextLine().trim();
        String report = manage.loadFile(path);
        System.out.println(report);
        manage.displayOrganisms();
    }

    // Add new organism manually via CLI
    public static void addOrganismCLI() {
        System.out.println("Enter a new organism:");

        //id portion of manually adding organism
        String id;
        while(true){
            System.out.println("Enter ID(make it 5 digits): ");
            id = scanner.nextLine();
            boolean validId = id.length() == 5;
            for (int i = 0; i < id.length() && validId; i++) {
                if (!Character.isDigit(id.charAt(i))) {
                    validId = false;
                }
            }
            if (!validId) {
                System.out.println("Invalid ID");
            }
            if (manage.idDuplicate(id)) {
                System.out.println("Id exists. Please try to enter a unique id: ");
                        continue;
            }
            break;
        }

        //clade name attribute addition
        System.out.print("Enter the Clade name: ");
        String clade = scanner.nextLine();

        //genusSpecies name
        System.out.println("Enter the genus and species information");
        String genusSpecies = scanner.nextLine();

        //lifespan estimate input
        int lifespanEstimate;
        while (true) {
            System.out.println("Enter Lifespan estimate as an integer");
            String lifespanString = scanner.nextLine();
           //ensure that the number entered is positive and an integer
            try {
                //convert this input into an integer
                lifespanEstimate = Integer.parseInt(lifespanString);
                if (lifespanEstimate <= 0) {
                    System.out.println("Lifespan needs to be a positive number.");
                    continue;
                }
                break;
            }
                catch (NumberFormatException e){
                        System.out.println("Invalid input. Please enter an integer.");
            }
        }

        //Lifespan unit input
        System.out.println("Enter the lifespan unit of measure (days, months, years, etc.): )");
        String lifespanUnit = scanner.nextLine();

        //Definitive features input
        System.out.println("Enter key definitive features of the organism: ");
        String definitiveFeatures = scanner.nextLine();

        //Average length of organism input
        float avgLength;
        while (true) {
            System.out.print("Enter Average Length (numeric form with decimals): ");
            String avgLengthStr = scanner.nextLine();
            //try catch to ensure input is in double form and positive
            try {
                avgLength = Float.parseFloat(avgLengthStr);
                if (avgLength <= 0) {
                    System.out.println("Average length must be positive.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }

        //Length unit input
        String lengthUnit;
        System.out.println("Enter Length Unit (mm, cm, or m):");
        while (true) {
            System.out.print("Enter Length Unit (m, cm, or mm): ");
            lengthUnit = scanner.nextLine();
            if (!lengthUnit.equals("m") && !lengthUnit.equals("cm") && !lengthUnit.equals("mm")) {
                System.out.println("Invalid input. Use m, cm, or mm.");
                continue;
            }
            break;
        }

        // Add organism via addOrganism method
        manage.addOrganism(new Organism(id, clade, genusSpecies, lifespanEstimate, lifespanUnit, definitiveFeatures, avgLength, lengthUnit));
        System.out.println("Organism added successfully!");

    }

    // Remove organism by its ID
    private static void removeOrganismCLI() {
        System.out.print("Enter 5-digit ID to remove: ");
        String id = scanner.nextLine();

        // Make sure the id is valid
        boolean validId = id.length() == 5;
        for (int i = 0; i < id.length() && validId; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                validId = false;
            }
        }
        if (!validId) {
            System.out.println("ID must be exactly 5 digits.");
            return;
        }

        //remove organism and show that it is removed or show that no ID was found
        Organism removed = manage.removeOrganism(id);
        if (removed != null) {
            System.out.println("Removed Organism: " + removed);
        } else {
            System.out.println("No organism found with that ID.");
        }

    }
    //CLI option to update organism going by id then choosing an attribute
    public static void updateOrganismCLI() {
        System.out.print("Enter 5-digit ID of organism to update: ");
        String id = scanner.nextLine();

        boolean validId = id.length() == 5;
        for (int i = 0; i < id.length() && validId; i++) {
            if (!Character.isDigit(id.charAt(i))) validId = false;
        }
        if (!validId) {
            System.out.println("Invalid ID format.");
            return;
        }

        //ask for attribute and give options
        System.out.println("Which attribute would you like to update?");
        System.out.println("Options: clade, genusSpecies, lifespan, lifespanUnit, features, averageLength, lengthUnit");
        String attribute = scanner.nextLine();

        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine();

        boolean updated = manage.updateOrganism(id, attribute, newValue);
        if (updated) {
            System.out.println("Organism updated successfully!");
        } else {
            System.out.println("Failed to update. Check ID, attribute, and value format.");
        }
    }
}
