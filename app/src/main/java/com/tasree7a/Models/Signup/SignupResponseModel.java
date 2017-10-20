package com.tasree7a.Models.Signup;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Models.Login.User;

import java.io.Serializable;

/**
 * Created by mac on 5/17/17.
 */

public class SignupResponseModel implements Serializable{

    @SerializedName("response_code")
    String responseCode;

    @SerializedName("response_message")
    String responseMessage;

    @SerializedName("details")
    SignUpDetailsModel userSignUpDetailsModel;

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setUserDetails(SignUpDetailsModel detailsModel) {
        this.userSignUpDetailsModel = detailsModel;
    }

    public SignUpDetailsModel getUserDetails() {
        return userSignUpDetailsModel;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
