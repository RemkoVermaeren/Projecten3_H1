package com.example.pdepu.veganapp_p3_h1.models;

import java.util.Date;

/**
 * Created by Kas on 13/08/2017.
 */

public class Blog {
    private String _id;
    private String name;
    private Date date;
    private String website;
    private String description;
    private String picture;
    private String author;

    public Blog(String id, String name, Date date, String website, String description, String picture, String author){

        _id = id;
        this.name = name;
        this.date = date;
        this.website = website;
        this.description = description;
        this.picture = picture;
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    protected void set_id(String _id) {
        this._id = _id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
