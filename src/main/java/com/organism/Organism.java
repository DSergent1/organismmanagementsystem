package com.organism;

/**Organism class to create organism object with attributes, getters and setters,
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

    // Constructor
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
    public String getId() {
        return id;
    }

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

    // toString method
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

