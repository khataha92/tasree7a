package com.tasree7a.Models.Signup;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Models.Login.User;

/**
 * Created by mac on 5/17/17.
 */

public class SignupResponseModel {

    @SerializedName("response_code")
    String responseCode;

    @SerializedName("response_message")
    String responseMessage;

    @SerializedName("details")
    User user;

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
