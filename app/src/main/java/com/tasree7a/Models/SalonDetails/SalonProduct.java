package com.tasree7a.Models.SalonDetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mac on 7/17/17.
 */

public class SalonProduct implements Serializable{

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("img_url")
    String url;

    @SerializedName("price")
    String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getPrice() {

        return price;
    }

    public void setPrice(String price) {

        this.price = price;
    }
}
