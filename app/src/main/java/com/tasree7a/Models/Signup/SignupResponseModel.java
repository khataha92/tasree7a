package com.tasree7a.Models.Signup;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 5/17/17.
 */

public class SignupResponseModel {

    @SerializedName("response_code")
    String responseCode;

    @SerializedName("response_message")
    String responseMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
