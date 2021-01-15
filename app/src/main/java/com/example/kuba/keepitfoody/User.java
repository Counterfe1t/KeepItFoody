package com.example.kuba.keepitfoody;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * This class is used to store currently logged in user's personal information.
 */
public class User implements Parcelable {

    private int ID;
    private String name = "";
    private String email = "";
    private String password = "";
    private String dateOfBirth = "";
    private String profilePicture = "";
    private String token = "";

    private boolean sex = true;

    private int basalMetabolicRate = 0;
    private double water = 0.0;
    private double weight = 0.0;
    private double height = 0.0;
    private double activityFactor = 0.0;
    private String allergens = "";

    // Constructor
    public User() {}

    protected User(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        dateOfBirth = in.readString();
        profilePicture = in.readString();
        token = in.readString();
        sex = in.readByte() != 0;
        basalMetabolicRate = in.readInt();
        water = in.readDouble();
        weight = in.readDouble();
        height = in.readDouble();
        activityFactor = in.readDouble();
        allergens = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(dateOfBirth);
        dest.writeString(profilePicture);
        dest.writeString(token);
        dest.writeByte((byte) (sex ? 1 : 0));
        dest.writeInt(basalMetabolicRate);
        dest.writeDouble(water);
        dest.writeDouble(weight);
        dest.writeDouble(height);
        dest.writeDouble(activityFactor);
        dest.writeString(allergens);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getActivityFactor() {
        return activityFactor;
    }

    public void setActivityFactor(double activityFactor) {
        this.activityFactor = activityFactor;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public boolean isSet() {
        if (dateOfBirth == null || height == 0.0 || weight == 0.0) {
            return false;
        } else {
            return true;
        }
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBasalMetabolicRate() {
        return basalMetabolicRate;
    }

    public void setBasalMetabolicRate(int basalMetabolicRate) {
        this.basalMetabolicRate = basalMetabolicRate;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    /**
     * Calculate and return user's age.
     * @return User's age
     */
    public int getAge() {
        Calendar calendar = Calendar.getInstance();
        String[] buffer = this.getDateOfBirth().split("-");
        return (calendar.get(Calendar.YEAR) - Integer.valueOf(buffer[0]));
    }
}
