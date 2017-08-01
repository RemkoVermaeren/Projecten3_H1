package com.example.pdepu.veganapp_p3_h1.models;

/**
 * Created by pdepu on 1/08/2017.
 */

public class Restaurant {

    private String name;
    private String address;
    private double rating;
    private String telephoneNumber;
    private String website;
    private String extraInformation;
    private boolean wheelchairAccess;
    private int veganPoints;

    public Restaurant(){

    }

    public Restaurant(String name, String address, double rating, String telephoneNumber, String website, String extraInformation, boolean wheelchairAccess, int veganPoints) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.telephoneNumber = telephoneNumber;
        this.website = website;
        this.extraInformation = extraInformation;
        this.wheelchairAccess = wheelchairAccess;
        this.veganPoints = veganPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getExtraInformation() {
        return extraInformation;
    }

    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    public boolean isWheelchairAccess() {
        return wheelchairAccess;
    }

    public void setWheelchairAccess(boolean wheelchairAccess) {
        this.wheelchairAccess = wheelchairAccess;
    }

    public int getVeganPoints() {
        return veganPoints;
    }

    public void setVeganPoints(int veganPoints) {
        this.veganPoints = veganPoints;
    }
}
