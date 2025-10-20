/**This Organism manager class will hold the methods for CRUD operations and the
 * custom action(finding average organism size grouped by clade).
It will also hold the method for reading a file formatted properly to enter new organisms
 to the data in app or in the database(phase4)
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OrganismManager {

        private ArrayList<Organism> organisms = new ArrayList<>();

        public ArrayList<Organism> getOrganisms(){
            return organisms;
        }

        //checks if the id entered is a duplicate
        public boolean idDuplicate(String id) {
            for (Organism organism : organisms) {
                if (organism.getId().equals(id)) {
                    return true;
                }
            }
            return false;
        }

        //method that adds an organism if the ID is unique
        public boolean addOrganism(Organism organism){
            if(organism == null || organism.getId().length() != 5){
                return false;
            }
            if(idDuplicate(organism.getId())) return false;
            organisms.add(organism);
            return true;
        }

        //method to remove a patron for the list by using their id
        public Organism removeOrganism(String id){
            for (int i = 0; i < organisms.size(); i++){
                if (organisms.get(i).getId().equals(id)) {
                    return organisms.remove(i);
                }
            }
            return  null;
        }

        //method to display all organisms
        public void displayOrganisms(){
            //rearranged display to show in a tabled format, dashes are for left-alignment
            System.out.printf("%-8s %-20s %-40s %-15s %-15s %-60s %-15s %-15s%n",
                    "ID |", "| Clade |", "| Genus & Species |", "| Lifespan |", "| Lifespan Unit |", "| Features |", "| Avg Length |", "| Length Unit|");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            // Table rows
            for (Organism organism : organisms) {
                System.out.printf("%-8s %-20s %-40s %-15d %-15s %-60s %-15.2f %-15s%n",
                        organism.getId(), organism.getCladeName(), organism.getGenusSpecies(),
                        organism.getLifespanEstimate(), organism.getLifespanUnit(), organism.getDefinitiveFeatures(),
                        organism.getAverageLength(), organism.getLengthUnit());
            }

        }

//method to update the organism, going through id, making sure its valid then selecting an attribute
    public boolean updateOrganism(String id, String attribute, String newValue) {
        for (Organism organism : organisms) {
            if (organism.getId().equals(id)) {
                switch (attribute.toLowerCase()) {
                    case "clade":
                    case "cladename":
                        organism.setCladeName(newValue);
                        return true;
                    case "genus":
                    case "genusspecies":
                        organism.setGenusSpecies(newValue);
                        return true;
                    case "lifespan":
                    case "lifespanestimate":
                        try {
                            int lifespan = Integer.parseInt(newValue);
                            organism.setLifespanEstimate(lifespan);
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    case "lifespanunit":
                        organism.setLifespanUnit(newValue);
                        return true;
                    case "features":
                    case "definitivefeatures":
                        organism.setDefinitiveFeatures(newValue);
                        return true;
                    case "average":
                    case "averagelength":
                        try {
                            float avg = Float.parseFloat(newValue);
                            organism.setAverageLength(avg);
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    case "lengthunit":
                        organism.setLengthUnit(newValue);
                        return true;
                    default:
                        return false;
                }
            }
        }
        return false;
    }
    //custom method that makes an array based on clade and gives them an average length
    public boolean displayAverageLengthByClade() {
        //Check if array is empty
        if (organisms.isEmpty()) {
            System.out.println("No organisms available.");
            return false;
        }
//create an array here to go by clades
        ArrayList<String> processedClades = new ArrayList<>();

        for (Organism organism : organisms) {
            String clade = organism.getCladeName();
            if (processedClades.contains(clade)) continue;

            float totalForCentimeters = 0;
            int count = 0;
            for (Organism org : organisms) {
                if (org.getCladeName().equals(clade)) {
                    float measurement = org.getAverageLength();
                    String unit = org.getLengthUnit().toLowerCase();
                    // Convert to cm here
                    switch (unit) {
                        case "m":
                            measurement *= 100;
                            break;
                        case "mm":
                            measurement /= 10;
                            break;
                    }
                    totalForCentimeters += measurement;
                    count++;
                }
            }
                float avg = totalForCentimeters / count;
                System.out.printf("Clade: %s | Average Length: %.2f cm%n", clade, avg);
                processedClades.add(clade);

        }
        return true;
    }

    //getter for testing custom method
    public float getCustomMethodAverage(String cladeName){
            float sumOfClade = 0;
            int divisor = 0;
            for (Organism organism : organisms){
                if(organism.getCladeName().equals(cladeName)){
                    float length = organism.getAverageLength();
                    String unitLength = organism.getLengthUnit();

                    switch(unitLength){
                        case "m": length*= 100; break;
                        case "mm": length/= 100; break;
                    }
                    sumOfClade += length;
                    divisor++;
                }
            }
            return divisor == 0 ? 0 : sumOfClade/divisor;

    }

//method for loading in organisms from a file
        public String loadFile(String filePath) {


            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                int lineNum = 0;

                //while loop that reads until there is nothing to input
                while ((line = br.readLine()) != null) {
                    lineNum++;
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    //split used here to separate the individual parts of each line by the dash
                    // id-cladeName-genusSpecies-lifespanEstimate-lifespanUnit-definiteFeatures-averageLength-lengthUnit
                    String[] parts = line.split("-", 8);
                    if (parts.length != 8) {
                        return "Line "+ lineNum+ ": is not in correct format.";
                    }

                    String id = parts[0].trim();
                    String cladeName = parts[1].trim();
                    String genusSpecies = parts[2].trim();
                    String lifespanEstimateStr = parts[3].trim();
                    String lifespanUnit = parts[4].trim();
                    String definiteFeatures = parts[5].trim();
                    String averageLengthStr = parts[6].trim();
                    String lengthUnit = parts[7].trim();


                    // Check to see if ID is 5 characters and is all in digits
                    boolean validId = id.length() == 5;
                    for (int i = 0; i < id.length() && validId; i++) {
                        if (!Character.isDigit(id.charAt(i))) {
                            validId = false;
                        }
                    }
                    if (!validId) {
                        return "Line " + lineNum + ": id should be 5 digits.";
                    }

                    //Checks id for duplicates
                    if (idDuplicate(id)) {
                        return "Line "+ lineNum+ ": has a duplicate ID.";
                    }

                    // Ensure numeric fields are numeric
                    double lifespanEstimate;
                    double averageLength;

                    try {
                        lifespanEstimate = Integer.parseInt(lifespanEstimateStr);
                        averageLength = Double.parseDouble(averageLengthStr);
                    } catch (NumberFormatException e) {
                        return "Line " + lineNum + ": needs a numbered lifespan or average length.";
                    }

                    if (lifespanEstimate <= 0) {
                        return "Line " + lineNum + ": lifespan needs to be be positive.";
                    }
                    if (averageLength <= 0) {
                        return "Line " + lineNum + ": average length should be positive.";
                    }


                    // Add the organism after validation
                    Organism o = new Organism(id, cladeName, genusSpecies, (int) lifespanEstimate,
                            lifespanUnit, definiteFeatures, (float) averageLength, lengthUnit);
                    organisms.add(o);
                }
                return "File uploaded correctly . "+ lineNum + " organisms were successfully added.";

            } catch (IOException e) {
                return "Error reading file: " + e.getMessage();
            }
        }
}
