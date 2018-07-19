package com.tasree7a.models.gallery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageModel implements Serializable {

    @SerializedName("url")
    private String imagePath;

    @SerializedName("id")
    private String imageId;

    private boolean isSelected;

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
