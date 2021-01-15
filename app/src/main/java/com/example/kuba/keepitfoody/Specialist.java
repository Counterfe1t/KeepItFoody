package com.example.kuba.keepitfoody;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a specialist such as a personal trainer,
 * or a dietitian.
 */
public class Specialist implements Parcelable {

    private int ID;
    private String profileImage;
    private String email;
    private String firstName;
    private String lastName;
    private String profession;
    private String specialization;
    private String picture;

    public Specialist() {
        // Empty public constructor
    }

    protected Specialist(Parcel in) {
        ID = in.readInt();
        profileImage = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        profession = in.readString();
        specialization = in.readString();
        picture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(profileImage);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(profession);
        dest.writeString(specialization);
        dest.writeString(picture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Specialist> CREATOR = new Creator<Specialist>() {
        @Override
        public Specialist createFromParcel(Parcel in) {
            return new Specialist(in);
        }

        @Override
        public Specialist[] newArray(int size) {
            return new Specialist[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
