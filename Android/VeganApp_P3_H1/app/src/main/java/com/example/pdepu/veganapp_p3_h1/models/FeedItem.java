package com.example.pdepu.veganapp_p3_h1.models;

/**
 * Created by maartenvanmeersche on 10/08/17.
 */

public class FeedItem {
    private String user;
    private Challenge challenge;

    public FeedItem(){

    }

    public FeedItem(String user, Challenge challenge){
        this.user = user;
        this.challenge = challenge;
    }

    public Challenge getChallenge(){
        return this.challenge;
    }

    public String getUser(){
        return this.user;
    }

    private void setChallenge(Challenge challenge){
        this.challenge = challenge;
    }

    private void setUser(String user){
        this.user = user;
    }

    @Override
    public String toString(){
        if (challenge.getName().toLowerCase().contains("recipe"))
            return user + " completed the challenge: " + challenge.getName() + " and made: " + challenge.getDescription();
        else if (challenge.getName().contains("restaurant"))
            return user + " completed the challenge: " + challenge.getName() + " and went to: " + challenge.getDescription();
        else
            return user + " completed the challenge: " + challenge.getName() + " and read: " + challenge.getDescription();

    }


}
