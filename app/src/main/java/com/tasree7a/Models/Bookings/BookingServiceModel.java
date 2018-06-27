package com.tasree7a.models.bookings;

/**
 * Created by mac on 8/31/17.
 */

public class BookingServiceModel {

    String serviceName;

    double cost;

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
