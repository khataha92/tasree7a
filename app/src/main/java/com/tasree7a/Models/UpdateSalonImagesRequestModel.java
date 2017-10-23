package com.tasree7a.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class UpdateSalonImagesRequestModel {

    @SerializedName("operation")
    String operation;

    @SerializedName("salonId")
    String salonId;

    @SerializedName("salonImage")
    String base64Image;

    @SerializedName("salonId")
    String imageId;


    public String getImageId() {

        return imageId;
    }


    public void setImageId(String imageId) {

        this.imageId = imageId;
    }


    public String getOperation() {

        return operation;
    }


    public void setOperation(String operation) {

        this.operation = operation;
    }


    public String getSalonId() {

        return salonId;
    }


    public void setSalonId(String salonId) {

        this.salonId = salonId;
    }


    public String getBase64Image() {

        return base64Image;
    }


    public void setBase64Image(String base64Image) {

        this.base64Image = base64Image;
    }
}
