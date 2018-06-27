package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamiKhleaf on 10/24/17.
 */

public class UpdateGalleryResponseModel {

    @SerializedName("response_code")
    private String responseCode;

    @SerializedName("response_message")
    private String responseMessage;

    @SerializedName("details")
    UpdateImagesResponseDetails details;


    public String getResponseCode() {

        return responseCode;
    }


    public void setResponseCode(String responseCode) {

        this.responseCode = responseCode;
    }


    public UpdateImagesResponseDetails getDetails() {

        return details;
    }


    public void setDetails(UpdateImagesResponseDetails details) {

        this.details = details;
    }


    public String getResponseMessage() {

        return responseMessage;
    }


    public void setResponseMessage(String responseMessage) {

        this.responseMessage = responseMessage;
    }
}
