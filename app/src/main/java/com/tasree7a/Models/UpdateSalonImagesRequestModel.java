package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class UpdateSalonImagesRequestModel {

    @SerializedName("operation")
    private
    String operation;

    @SerializedName("salonId")
    private
    String salonId;

    @SerializedName("salonImage")
    private
    String base64Image;

    @SerializedName("product_ids_list")
    private
    List<String> imageId;


    public List<String> getImageId() {
        return imageId;
    }

    public void setImageId(List<String> imageId) {
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
