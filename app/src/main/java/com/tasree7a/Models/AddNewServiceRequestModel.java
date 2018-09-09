package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamiKhleaf on 10/24/17.
 */

public class AddNewServiceRequestModel {

    @SerializedName("serviceName")
    String serviceName;

    @SerializedName("servicePrice")
    String servicePrice;

    @SerializedName("salonId")
    String salonId;

    @SerializedName("serviceDuration")
    String serviceDuration;

    public String getServiceName() {

        return serviceName;
    }

    public AddNewServiceRequestModel setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public AddNewServiceRequestModel setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
        return this;
    }

    public String getSalonId() {
        return salonId;
    }

    public AddNewServiceRequestModel setSalonId(String salonId) {
        this.salonId = salonId;
        return this;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public AddNewServiceRequestModel setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
        return this;
    }
}
