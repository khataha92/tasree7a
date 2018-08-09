package com.tasree7a.models.bookings;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 8/31/17.
 */

public class BookingServiceModel {

    @SerializedName("name")
    String serviceName;

    @SerializedName("price")
    double cost;

    @SerializedName("url")
    String imageUrl;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
