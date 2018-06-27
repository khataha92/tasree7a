package com.tasree7a.models.salonbooking;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Constants;

/**
 * Created by mac on 8/27/17.
 */

public class SalonService {

    String id;

    @SerializedName("picture")
    String imageUrl;

    String name;

    double price;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {

        return Constants.IMAGE_PREFIX + imageUrl;

    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
