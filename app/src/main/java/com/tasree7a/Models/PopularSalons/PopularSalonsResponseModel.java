package com.tasree7a.Models.PopularSalons;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Enums.ResponseCode;
import com.tasree7a.Models.SalonDetails.SalonModel;

import java.util.List;

/**
 * Created by mac on 6/20/17.
 */

public class
PopularSalonsResponseModel {

    @SerializedName("response_code")
    ResponseCode responseCode;

    @SerializedName("salons")
    List<SalonModel> salons;

    public List<SalonModel> getSalons() {
        return salons;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
