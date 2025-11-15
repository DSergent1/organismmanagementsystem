package com.organism;

/**
 * This class is used to make an organism with information in attributes such as: clade names, genus & species,
 * defining features, and measurements.
 *
 * Organism class to create organism object with attributes, getters and setters,
 * a constructor, and a toString() method.
 */

public class Organism {
    // Attributes
    private String id;
    private String cladeName;
    private String genusSpecies;
    private int lifespanEstimate;
    private String lifespanUnit;
    private String definitiveFeatures;
    private float averageLength;
    private String lengthUnit;

    /**
     * Constructor for a new Organism with all required attributes.
     *
     * @param id                  organism ID/primary key
     * @param cladeName           scientific clade/group name
     * @param genusSpecies        genus and species name
     * @param lifespanEstimate    estimated lifespan of organism
     * @param lifespanUnit        measurement unit for lifespan (years, minutes)
     * @param definitiveFeatures  notable physical or behavioral features of the organism
     * @param averageLength       average length value
     * @param lengthUnit          unit for the average length (cm, m, or mm)
     */
    public Organism(String id, String cladeName, String genusSpecies, int lifespanEstimate, String lifespanUnit,
                    String definitiveFeatures, float averageLength, String lengthUnit) {
        this.id = id;
        this.cladeName = cladeName;
        this.genusSpecies = genusSpecies;
        this.lifespanEstimate = lifespanEstimate;
        this.lifespanUnit = lifespanUnit;
        this.definitiveFeatures = definitiveFeatures;
        this.averageLength = averageLength;
        this.lengthUnit = lengthUnit;
    }

    // Default constructor
    public Organism() {}

    // Getters and Setters
    /** Example for getters and setters
     * Returns the ID of the organism.
     *
     * @return the organism's ID.
     */
    public String getId() {
        return id;
    }
    /**
     * Updates the organism's ID.
     *
     * @param id is the new ID to assign.
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getCladeName() {
        return cladeName;
    }

    public void setCladeName(String cladeName) {
        this.cladeName = cladeName;
    }

    public String getGenusSpecies() {
        return genusSpecies;
    }

    public void setGenusSpecies(String genusSpecies) {
        this.genusSpecies = genusSpecies;
    }

    public int getLifespanEstimate() {
        return lifespanEstimate;
    }

    public void setLifespanEstimate(int lifespanEstimate) {
        this.lifespanEstimate = lifespanEstimate;
    }

    public String getLifespanUnit() {
        return lifespanUnit;
    }

    public void setLifespanUnit(String lifespanUnit) {
        this.lifespanUnit = lifespanUnit;
    }

    public String getDefinitiveFeatures() {
        return definitiveFeatures;
    }

    public void setDefinitiveFeatures(String definitiveFeatures) {
        this.definitiveFeatures = definitiveFeatures;
    }

    public float getAverageLength() {
        return averageLength;
    }

    public void setAverageLength(float averageLength) {
        this.averageLength = averageLength;
    }

    public String getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    /**
     * Returns a string of the organism with all the attributes.
     *
     * @return organism information formatted.
     */
    @Override
    public String toString() {
        return "Organism{" +
                "id=" + id +
                ", cladeName='" + cladeName + '\'' +
                ", genusSpecies='" + genusSpecies + '\'' +
                ", lifespanEstimate=" + lifespanEstimate +
                ", lifespanUnit='" + lifespanUnit + '\'' +
                ", definitiveFeatures='" + definitiveFeatures + '\'' +
                ", averageLength=" + averageLength +
                ", lengthUnit='" + lengthUnit + '\'' +
                '}';
    }
}

