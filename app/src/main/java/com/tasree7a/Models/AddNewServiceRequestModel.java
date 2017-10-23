package com.tasree7a.Models;

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

    @SerializedName("serviceImage")
    String serviceImage;


    public String getServiceName() {

        return serviceName;
    }


    public void setServiceName(String serviceName) {

        this.serviceName = serviceName;
    }


    public String getServicePrice() {

        return servicePrice;
    }


    public void setServicePrice(String servicePrice) {

        this.servicePrice = servicePrice;
    }


    public String getSalonId() {

        return salonId;
    }


    public void setSalonId(String salonId) {

        this.salonId = salonId;
    }


    public String getServiceImage() {

        return serviceImage;
    }


    public void setServiceImage(String serviceImage) {

        this.serviceImage = serviceImage;
    }
}
