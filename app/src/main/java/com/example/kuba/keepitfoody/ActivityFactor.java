package com.example.kuba.keepitfoody;

/**
 * This class represent weekly activity factor.
 */
public class ActivityFactor {
    private final int ID;
    private String name;
    private double maleFactor;
    private double femaleFactor;

    public ActivityFactor(int ID, String name, double maleFactor, double femaleFactor) {
        this.ID = ID;
        this.name = name;
        this.maleFactor = maleFactor;
        this.femaleFactor = femaleFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaleFactor() {
        return maleFactor;
    }

    public void setMaleFactor(double maleFactor) {
        this.maleFactor = maleFactor;
    }

    public double getFemaleFactor() {
        return femaleFactor;
    }

    public void setFemaleFactor(double femaleFactor) {
        this.femaleFactor = femaleFactor;
    }

    public int getID() {
        return ID;
    }
}
