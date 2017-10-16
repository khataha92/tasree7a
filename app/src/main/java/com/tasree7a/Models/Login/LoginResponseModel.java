package com.tasree7a.Models.Login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 5/17/17.
 */

public class LoginResponseModel {

    @SerializedName("response_code")
    String responseCode;

    @SerializedName("response_message")
    String responseMessage;

    @SerializedName("details")
    User user;

    @SerializedName("salon_id")
    String salonId;

    public String getSalonId() {
        return salonId;
    }

    public User getUser() {
        return user;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
