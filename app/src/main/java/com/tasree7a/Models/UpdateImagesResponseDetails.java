package com.tasree7a.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamiKhleaf on 10/24/17.
 */

public class UpdateImagesResponseDetails {

    @SerializedName("sallon_id")
    String salonId;

    @SerializedName("image_id")
    String imageId;

    @SerializedName("id")
    String id;


    public String getSalonId() {

        return salonId;
    }


    public void setSalonId(String salonId) {

        this.salonId = salonId;
    }


    public String getImageId() {

        return imageId;
    }


    public void setImageId(String imageId) {

        this.imageId = imageId;
    }


    public String getId() {

        return id;
    }


    public void setId(String id) {

        this.id = id;
    }
}
