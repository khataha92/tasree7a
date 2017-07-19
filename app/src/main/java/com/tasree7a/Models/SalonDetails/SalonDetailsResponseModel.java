package com.tasree7a.Models.SalonDetails;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Enums.ResponseCode;
import com.tasree7a.Models.PopularSalons.SalonModel;

import java.util.List;

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
