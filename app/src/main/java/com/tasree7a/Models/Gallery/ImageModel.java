package com.tasree7a.Models.Gallery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mac on 6/14/17.
 */

public class ImageModel implements Serializable{

    @SerializedName("url")
    String imagePath;

    @SerializedName("id")
    String imageId;


    public String getImageId() {

        return imageId;
    }


    public void setImageId(String imageId) {

        this.imageId = imageId;
    }


    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
