package com.example.pdepu.veganapp_p3_h1.models;

/**
 * Created by pdepu on 4/08/2017.
 */

public class Token {
    private String Token;
    private String userid;

    public Token() {
    }

    public Token(String token, String userid) {
        this.Token = token;
        this.userid = userid;
    }

    public String getToken() {
        return this.Token;
    }

    public void setToken(String token) {
        this.Token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
