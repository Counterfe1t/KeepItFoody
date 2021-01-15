package com.example.kuba.keepitfoody;

/**
 * This class represents type of meal.
 */
public class MealType {
    private String name;
    private int icon;

    public MealType(String name, int icon) {
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
