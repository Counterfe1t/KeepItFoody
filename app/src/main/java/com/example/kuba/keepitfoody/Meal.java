package com.example.kuba.keepitfoody;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a meal along with necessary
 * properties (such as name, date, status) and a collection
 * of Ingredient type objects.
 */
public class Meal implements Parcelable {

    private int ID;
    private int RECIPE_ID;
    private String name;
    private String date;
    private String time;
    private int icon;
    private boolean status = false;

    // List of ingredients with quantities
    private ArrayList<Ingredient> ingredients;

    private int energy;
    private int protein;
    private int fats;
    private int carbohydrates;
    private int fibre;
    private int salt;

    // Constructor
    public Meal() {
        this.ingredients = new ArrayList<>();
    }

    public Meal(Recipe recipe) {
        this.name = recipe.getName();
        //this.ingredients = recipe.getIngredients();
        this.ingredients = new ArrayList<>();
    }

    protected Meal(Parcel in) {
        ID = in.readInt();
        RECIPE_ID = in.readInt();
        name = in.readString();
        date = in.readString();
        time = in.readString();
        icon = in.readInt();
        status = in.readByte() != 0;
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        energy = in.readInt();
        protein = in.readInt();
        fats = in.readInt();
        carbohydrates = in.readInt();
        fibre = in.readInt();
        salt = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(RECIPE_ID);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeInt(icon);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeTypedList(ingredients);
        dest.writeInt(energy);
        dest.writeInt(protein);
        dest.writeInt(fats);
        dest.writeInt(carbohydrates);
        dest.writeInt(fibre);
        dest.writeInt(salt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRECIPE_ID() {
        return RECIPE_ID;
    }

    public void setRECIPE_ID(int RECIPE_ID) {
        this.RECIPE_ID = RECIPE_ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        //if (ingredient.getQuantity() != 0) {
            ingredients.add(ingredient);
        //}
    }

    public int getEnergy() {
        double energy = 0;
        if (!ingredients.isEmpty()) {
            for (Ingredient ingredient : ingredients) {
                energy += (ingredient.getQuantity() * ingredient.getEnergy() / 100);
            }
        }

        BigDecimal bd = new BigDecimal(energy);
        bd = bd.setScale(0, RoundingMode.HALF_UP);
        return bd.intValue();
    }

    public double getProtein() {
        double protein = 0;
        if (!ingredients.isEmpty()) {
            for (Ingredient ingredient : ingredients) {
                protein += (ingredient.getQuantity() * ingredient.getProtein() / 100);
            }
        }

        BigDecimal bd = new BigDecimal(protein);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getFats() {
        double fats = 0;
        if (!ingredients.isEmpty()) {
            for (Ingredient ingredient : ingredients) {
                fats += (ingredient.getQuantity() * ingredient.getFats() / 100);
            }
        }
        BigDecimal bd = new BigDecimal(fats);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getCarbohydrates() {
        double carbohydrates = 0;
        if (!ingredients.isEmpty()) {
            for (Ingredient ingredient : ingredients) {
                carbohydrates += (ingredient.getQuantity() * ingredient.getCarbohydrates() / 100);
            }
        }
        BigDecimal bd = new BigDecimal(carbohydrates);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getFibre() {
        double fibre = 0;
        if (!ingredients.isEmpty()) {
            for (Ingredient ingredient : ingredients) {
                fibre += (ingredient.getQuantity() * ingredient.getFibre() / 100);
            }
        }
        BigDecimal bd = new BigDecimal(fibre);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getSalt() {
        double salt = 0;
        if (!ingredients.isEmpty()) {
            for (Ingredient ingredient : ingredients) {
                salt += (ingredient.getQuantity() * ingredient.getSalt() / 100);
            }
        }
        BigDecimal bd = new BigDecimal(salt);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
