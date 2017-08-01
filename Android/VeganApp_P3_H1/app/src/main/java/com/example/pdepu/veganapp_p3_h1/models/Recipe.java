package com.example.pdepu.veganapp_p3_h1.models;

/**
 * Created by pdepu on 1/08/2017.
 */

public class Recipe {
    private String name;
    private int veganPoints;
    private double calories;
    private String[] food;
    private String difficulty;
    private double time;
    private String[] allergies;
    private String picture;
    private String type;
    private String[] instructions;


    public Recipe() {
    }

    public Recipe(String name, int veganPoints, double calories, String[] food, String difficulty, double time, String[] allergies, String picture, String type, String[] instructions) {
        this.name = name;
        this.veganPoints = veganPoints;
        this.calories = calories;
        this.food = food;
        this.difficulty = difficulty;
        this.time = time;
        this.allergies = allergies;
        this.picture = picture;
        this.type = type;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVeganPoints() {
        return veganPoints;
    }

    public void setVeganPoints(int veganPoints) {
        this.veganPoints = veganPoints;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public String[] getFood() {
        return food;
    }

    public void setFood(String[] food) {
        this.food = food;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String[] getAllergies() {
        return allergies;
    }

    public void setAllergies(String[] allergies) {
        this.allergies = allergies;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }
}
