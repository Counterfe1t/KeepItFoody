package com.example.kuba.keepitfoody;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * This class represents a recipe along with necessary
 * properties (such as name, image, description, difficulty) and a collection
 * of Ingredient type objects.
 */
public class Recipe implements Parcelable {

    private int ID;
    private String name;
    private String description;
    private String image;
    private int preparationTime;
    private int difficulty;
    private int AUTHOR_ID;
    private String authorImage;
    private String authorFirstName;
    private ArrayList<Ingredient> ingredients;

    // Constructor
    public Recipe() {}

    public Recipe(int ID, String name, String description, String image, int preparationTime, int difficulty) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.image = image;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
    }

    // Getters and Setters

    protected Recipe(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        preparationTime = in.readInt();
        difficulty = in.readInt();
        AUTHOR_ID = in.readInt();
        authorImage = in.readString();
        authorFirstName = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeInt(preparationTime);
        dest.writeInt(difficulty);
        dest.writeInt(AUTHOR_ID);
        dest.writeString(authorImage);
        dest.writeString(authorFirstName);
        dest.writeTypedList(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getAUTHOR_ID() {
        return AUTHOR_ID;
    }

    public void setAUTHOR_ID(int AUTHOR_ID) {
        this.AUTHOR_ID = AUTHOR_ID;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
