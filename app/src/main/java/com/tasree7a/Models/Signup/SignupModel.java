package com.tasree7a.Models.Signup;

/**
 * Created by mac on 5/17/17.
 */

public class SignupModel {

    String firstName,lastName,email,username,password, fbId;

    boolean isFbLogin, isBuisness;

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbId() {
        return fbId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFbLogin() {
        return isFbLogin;
    }

    public void setFbLogin(boolean fbLogin) {
        isFbLogin = fbLogin;
    }

    public boolean isBuisness() { return isBuisness; }

    public void setBuisness(boolean buisness) { isBuisness = buisness; }
}
