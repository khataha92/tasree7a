package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;

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

    public String getOperation() {
        return operation;
    }

    public UpdateSalonImagesRequestModel setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public String getSalonId() {
        return salonId;
    }

    public UpdateSalonImagesRequestModel setSalonId(String salonId) {
        this.salonId = salonId;
        return this;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public UpdateSalonImagesRequestModel setBase64Image(String base64Image) {
        this.base64Image = base64Image;
        return this;
    }

    public List<String> getImageId() {
        return imageId;
    }

    public UpdateSalonImagesRequestModel setImageId(List<String> imageId) {
        this.imageId = imageId;
        return this;
    }
}
