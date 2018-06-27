package com.tasree7a.models.popularsalons;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.enums.ResponseCode;
import com.tasree7a.models.salondetails.SalonModel;

import java.util.List;

/**
 * Created by mac on 6/20/17.
 */

public class PopularSalonsResponseModel {

    @SerializedName("response_code")
    private ResponseCode responseCode;

    @SerializedName("salons")
    private List<SalonModel> salons;

    public List<SalonModel> getSalons() {
        return salons;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
