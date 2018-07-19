package com.tasree7a.models.salondetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SalonProduct implements Serializable {

    @SerializedName("id")
    private
    String id;

    @SerializedName("name")
    private
    String name;

    @SerializedName("description")
    private
    String description;

    @SerializedName("img_url")
    private
    String url;

    @SerializedName("price")
    private
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