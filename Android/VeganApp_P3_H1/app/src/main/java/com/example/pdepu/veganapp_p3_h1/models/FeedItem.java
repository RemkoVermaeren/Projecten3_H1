package com.example.pdepu.veganapp_p3_h1.models;

/**
 * Created by maartenvanmeersche on 10/08/17.
 */

public class FeedItem {
    private User user;
    private Challenge challenge;

    public FeedItem(){

    }

    public FeedItem(User user, Challenge challenge){
        this.user = user;
        this.challenge = challenge;
    }

    public Challenge getChallenge(){
        return this.challenge;
    }

    public User getUser(){
        return this.user;
    }

    private void setChallenge(Challenge challenge){
        this.challenge = challenge;
    }

    private void setUser(User user){
        this.user = user;
    }

    @Override
    public String toString(){
        return user.getSurName() + " completed the challenge: " + challenge.getName();
    }
}
