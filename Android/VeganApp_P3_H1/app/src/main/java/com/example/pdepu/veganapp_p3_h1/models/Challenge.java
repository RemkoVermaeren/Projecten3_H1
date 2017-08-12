package com.example.pdepu.veganapp_p3_h1.models;

import java.util.Date;

/**
 * Created by pdepu on 1/08/2017.
 */

public class Challenge {
    private String _id;
    private String name;
    private String description;
    private String picture;
    private Date date;
    private int amountOfLikes;
    private int veganScore;
    private boolean isCompleted;

    public Challenge(){

    }

    public Challenge(String _id, String name, String description, String picture, Date date, int amountOfLikes, int veganScore, boolean isCompleted) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.date = date;
        this.amountOfLikes = amountOfLikes;
        this.veganScore = veganScore;
        this.isCompleted = isCompleted;
    }

    public Challenge(String name, String description, String picture, Date date, int amountOfLikes, int veganScore, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.date = date;
        this.amountOfLikes = amountOfLikes;
        this.veganScore = veganScore;
        this.isCompleted = isCompleted;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmountOfLikes() {
        return amountOfLikes;
    }

    public void setAmountOfLikes(int amountOfLikes) {
        this.amountOfLikes = amountOfLikes;
    }

    public int getVeganScore() {
        return veganScore;
    }

    public void setVeganScore(int veganScore) {
        this.veganScore = veganScore;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
