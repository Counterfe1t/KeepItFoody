package com.example.kuba.keepitfoody;

/**
 * This class represents ingredient's category.
 */
public class IngredientCategory {

    private String name;
    private int icon;

    public IngredientCategory(String name, int icon) {
        this.name = name;
        this.icon = icon;
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

}
