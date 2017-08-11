package com.example.pdepu.veganapp_p3_h1.models;

/**
 * Created by maartenvanmeersche on 10/08/17.
 */

public class FeedItem {
    public User user;
    public Challenge challenge;

    public FeedItem(){

    }

    public FeedItem(User user, Challenge challenge){
        this.user = user;
        this.challenge = challenge;
    }

    @Override
    public String toString(){
        return user.getSurName() + " just completed the challenge: " + challenge.getName();
    }
}
