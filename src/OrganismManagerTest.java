import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class OrganismManagerTest {


    //create object to be tested
    Organism organism;
    OrganismManager manage;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        //supply test data to an organism object
        manage = new OrganismManager();
        organism = new Organism("00021", "Lycophyta", "Lepidodendron", 15, "years",
                "extinct, prehistoric", 50.5F, "m" );
    }

    @org.junit.jupiter.api.Test
    @DisplayName("add organism test")
    void addOrganism() {
        manage.addOrganism(organism);

        assertEquals( 1, manage.getOrganisms().size(), "Error: organism was not added correctly.");

    }

    @org.junit.jupiter.api.Test
    @DisplayName("add organism test")
    void addOrganismFail() {
        manage.addOrganism(new Organism("999921", "Lycophyta", "Lepidodendron", 15, "years",
                "extinct, prehistoric", 50.5F, "m"));

        assertEquals( 1, manage.getOrganisms().size(), "Error: organism was not added correctly.");

    }

    @org.junit.jupiter.api.Test
    void removeOrganism() {
        manage.addOrganism(organism);
        Organism removedOrganism = manage.removeOrganism("00021");
        assertEquals("00021", removedOrganism.getId());
        assertEquals(0, manage.getOrganisms().size());
    }

    @org.junit.jupiter.api.Test
    void removeOrganismFail() {
        manage.addOrganism(organism);
        Organism removedOrganism = manage.removeOrganism("00022");
        assertEquals("00021", removedOrganism.getId());
        assertEquals(0, manage.getOrganisms().size());
    }


    @org.junit.jupiter.api.Test
    void updateOrganism() {
        manage.addOrganism(organism);
        boolean updatedOrganismTest = manage.updateOrganism("00021", "lengthunit", "cm");
        assertTrue(updatedOrganismTest, "True when update is successful.");
        assertEquals("cm", manage.getOrganisms().get(0).getLengthUnit(),"Length unit should be updated.");
    }

    @org.junit.jupiter.api.Test
    void updateOrganismFail() {
        manage.addOrganism(organism);
        boolean updatedOrganismTest = manage.updateOrganism("00021", "lengthunit", "in");
        assertTrue(updatedOrganismTest, "True when update is successful.");
        assertEquals("cm", manage.getOrganisms().get(0).getLengthUnit(),"Length unit should be updated.");
    }


    @org.junit.jupiter.api.Test
    void displayAverageLengthByClade() {
        manage.addOrganism(organism);
        manage.addOrganism(new Organism("00015", "Lycophyta","Selaginella",
                5, "years", "scalelike leaves, strobili", 8.0F,"cm"));
        assertEquals(2529.00, manage.getCustomMethodAverage("Lycophyta"));
    }

    @org.junit.jupiter.api.Test
    void displayAverageLengthByCladeFail() {
        manage.addOrganism(organism);
        manage.addOrganism(new Organism("00015", "Lycophyta","Selaginella",
                5, "years", "scalelike leaves, strobili", 72F,"cm"));
        assertEquals(2529.00, manage.getCustomMethodAverage("Lycophyta"));
    }

    @org.junit.jupiter.api.Test
    void loadFile() {
        manage.loadFile("C:\\Users\\danie\\Downloads\\organismsheet.txt");
        assertFalse(manage.getOrganisms().isEmpty());
    }

    @org.junit.jupiter.api.Test
    void loadFileFail() {
        manage.loadFile("C:\\Users\\danie\\Downloads\\orgsheet.txt");
        assertFalse(manage.getOrganisms().isEmpty());
    }
}

