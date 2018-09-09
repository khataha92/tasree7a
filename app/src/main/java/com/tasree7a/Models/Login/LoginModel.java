package com.tasree7a.models.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 5/17/17.
 */

public class LoginModel {

    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("fb_flag")
    boolean isFacebookLogin;
    @SerializedName("isBusiness")
    boolean isBusiness;

    public String getUsername() {
        return username;
    }

    public LoginModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isFacebookLogin() {
        return isFacebookLogin;
    }

    public LoginModel setFacebookLogin(boolean facebookLogin) {
        isFacebookLogin = facebookLogin;
        return this;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public LoginModel setBusiness(boolean business) {
        isBusiness = business;
        return this;
    }
}
