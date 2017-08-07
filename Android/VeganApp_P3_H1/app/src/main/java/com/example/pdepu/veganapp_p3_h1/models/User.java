package com.example.pdepu.veganapp_p3_h1.models;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by pdepu on 1/08/2017.
 */

public class User {


    private String _id;
    private String username;
    private String name;
    private String surName;
    private int totalVeganScore;
    private User[] followingUsers;
    private Date dateOfCreation;
    private Challenge[] challenges;
    private boolean isAdmin;
    private String hash;
    private String salt;
    private String image;
    private String password;
    private Token token;
    private String fullName;

    public User(){

    }


    public User(String username, String name, String surName, Date dateOfCreation, boolean isAdmin, String password) {
        this.username = username;
        this.name = name;
        this.surName = surName;
        this.dateOfCreation = dateOfCreation;
        this.isAdmin = isAdmin;
        this.password = password;
    }

    public User(String _id,String username, String name, String surName, int totalVeganScore, User[] followingUsers, Date dateOfCreation, Challenge[] challenges, boolean isAdmin, String hash, String salt, String image, String fullName) {
        this._id = _id;
        this.username = username;
        this.name = name;
        this.surName = surName;
        this.totalVeganScore = totalVeganScore;
        this.followingUsers = followingUsers;
        this.dateOfCreation = dateOfCreation;
        this.challenges = challenges;
        this.isAdmin = isAdmin;
        this.hash = hash;
        this.salt = salt;
        this.image = image;
        this.fullName = fullName;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public int getTotalVeganScore() {
        return totalVeganScore;
    }

    public void setTotalVeganScore(int totalVeganScore) {
        this.totalVeganScore = totalVeganScore;
    }

    public User[] getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(User[] followingUsers) {
        this.followingUsers = followingUsers;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Challenge[] getChallenges() {
        return challenges;
    }

    public void setChallenges(Challenge[] challenges) {
        this.challenges = challenges;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (totalVeganScore != user.totalVeganScore) return false;
        if (isAdmin != user.isAdmin) return false;
        if (_id != null ? !_id.equals(user._id) : user._id != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (surName != null ? !surName.equals(user.surName) : user.surName != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(followingUsers, user.followingUsers)) return false;
        if (dateOfCreation != null ? !dateOfCreation.equals(user.dateOfCreation) : user.dateOfCreation != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(challenges, user.challenges)) return false;
        if (hash != null ? !hash.equals(user.hash) : user.hash != null) return false;
        if (salt != null ? !salt.equals(user.salt) : user.salt != null) return false;
        if (image != null ? !image.equals(user.image) : user.image != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (token != null ? !token.equals(user.token) : user.token != null) return false;
        return fullName != null ? fullName.equals(user.fullName) : user.fullName == null;

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surName != null ? surName.hashCode() : 0);
        result = 31 * result + totalVeganScore;
        result = 31 * result + Arrays.hashCode(followingUsers);
        result = 31 * result + (dateOfCreation != null ? dateOfCreation.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(challenges);
        result = 31 * result + (isAdmin ? 1 : 0);
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }
}
