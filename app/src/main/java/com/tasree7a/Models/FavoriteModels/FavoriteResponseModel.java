package com.tasree7a.models.favoritemodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SamiKhleaf on 8/6/17.
 */

public class FavoriteResponseModel {

    @SerializedName("response_code")
    String responseCode;

    @SerializedName("response_message")
    String responseMessage;

    @SerializedName("details")
    List<FavoriteDetailsModel> details;


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


    public List<FavoriteDetailsModel> getDetails() {

        return details;
    }


    public void setDetails(List<FavoriteDetailsModel> details) {

        this.details = details;
    }
}
