package com.example.kuba.keepitfoody;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents an ingredient along with
 * it's nutrition details.
 */
public class Ingredient implements Parcelable {

    // Basic Information
    private int ID;
    private String name;
    private String category;
    private int icon;

    // Nutrition and quantity
    private int quantity;

    private double energy;
    private double protein;
    private double fats;
    private double carbohydrates;
    private double fibre;
    private double salt;

    // Details
    private String description;
    private boolean gluten;
    private boolean lactose;

    // Constructors
    public Ingredient() {}

    public Ingredient(int ID,
                      String name,
                      String category,
                      int icon,
                      double energy,
                      double protein,
                      double fats,
                      double carbohydrates,
                      double fibre,
                      double salt,
                      String description,
                      boolean gluten,
                      boolean lactose) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.icon = icon;
        this.quantity = 0;
        this.energy = energy;
        this.protein = protein;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.fibre = fibre;
        this.salt = salt;
        this.description = description;
        this.gluten = gluten;
        this.lactose = lactose;
    }

    protected Ingredient(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        category = in.readString();
        icon = in.readInt();
        quantity = in.readInt();
        energy = in.readDouble();
        protein = in.readDouble();
        fats = in.readDouble();
        carbohydrates = in.readDouble();
        fibre = in.readDouble();
        salt = in.readDouble();
        description = in.readString();
        gluten = in.readByte() != 0;
        lactose = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeInt(icon);
        dest.writeInt(quantity);
        dest.writeDouble(energy);
        dest.writeDouble(protein);
        dest.writeDouble(fats);
        dest.writeDouble(carbohydrates);
        dest.writeDouble(fibre);
        dest.writeDouble(salt);
        dest.writeString(description);
        dest.writeByte((byte) (gluten ? 1 : 0));
        dest.writeByte((byte) (lactose ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    // Setters and Getters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFibre() {
        return fibre;
    }

    public void setFibre(double fibre) {
        this.fibre = fibre;
    }

    public double getSalt() {
        return salt;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGluten() {
        return gluten;
    }

    public void setGluten(boolean gluten) {
        this.gluten = gluten;
    }

    public boolean isLactose() {
        return lactose;
    }

    public void setLactose(boolean lactose) {
        this.lactose = lactose;
    }
}
