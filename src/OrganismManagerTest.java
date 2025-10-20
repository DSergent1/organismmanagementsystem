import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class OrganismManagerTest {


    //create object to be tested
    Organism organism;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        //supply test data to an organism object
        organism = new Organism("00021", "Lycophyta", "Lycopdoium", 12, "years",
                "one large chloroplast per photosynthetic cell", 18.12F, "cm" );
    }

    @org.junit.jupiter.api.Test
    @DisplayName("add organism test")
    void addOrganism() {

    }

    @org.junit.jupiter.api.Test
    void removeOrganism() {
    }

    @org.junit.jupiter.api.Test
    void updateOrganism() {
    }

    @org.junit.jupiter.api.Test
    void displayAverageLengthByClade() {
    }

    @org.junit.jupiter.api.Test
    void loadFile() {
    }
}