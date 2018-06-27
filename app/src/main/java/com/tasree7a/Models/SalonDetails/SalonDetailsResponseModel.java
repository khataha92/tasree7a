package com.tasree7a.models.salondetails;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.enums.ResponseCode;

/**
 * Created by mac on 7/18/17.
 */

public class SalonDetailsResponseModel {

    @SerializedName("response_code")
    ResponseCode responseCode;

    @SerializedName("details")
    SalonModel salon;

    public SalonModel getSalon() {
        return salon;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
