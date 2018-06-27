package com.tasree7a.models.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 5/17/17.
 */

public class LoginResponseModel {

    @SerializedName("response_code")
    private String responseCode;

    @SerializedName("response_message")
    private String responseMessage;

    @SerializedName("details")
    private User user;

    @SerializedName("salon_id")
    private String salonId;

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
