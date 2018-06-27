package com.tasree7a.models.salonbooking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mac on 10/9/17.
 */

public class SalonServicesResponse {

    @SerializedName("response_code")
    String responseCode;

    @SerializedName("details")
    List<SalonService> services;

    public List<SalonService> getServices() {
        return services;
    }


}
