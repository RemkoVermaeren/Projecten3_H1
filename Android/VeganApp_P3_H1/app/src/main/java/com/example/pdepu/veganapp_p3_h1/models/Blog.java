package com.example.pdepu.veganapp_p3_h1.models;

import java.util.Date;

/**
 * Created by Kas on 13/08/2017.
 */

public class Blog {
    private String _id;
    private String title;
    private Date publishDate;
    private String website;
    private String description;

    public Blog(){

    }

    public Blog(String id, String title, Date publishDate, String website, String description) {
        _id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.website = website;
        this.description = description;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    protected void set_id(String _id) {
        this._id = _id;
    }
}
