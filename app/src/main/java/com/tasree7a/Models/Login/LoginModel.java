package com.tasree7a.Models.Login;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFacebookLogin() {
        return isFacebookLogin;
    }

    public void setFacebookLogin(boolean facebookLogin) {
        isFacebookLogin = facebookLogin;
    }

    public boolean isBusiness() { return isBusiness; }

    public void setBusiness(boolean business) { isBusiness = business; }
}
