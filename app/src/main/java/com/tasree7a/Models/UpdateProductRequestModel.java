package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class UpdateProductRequestModel {

    @SerializedName("operation")
    private String operation; // ADD/Remove/Update

    @SerializedName("productName")
    private String productName;

    @SerializedName("productDescription")
    private String productDescription;

    @SerializedName("productPrice")
    private String productPrice;

    @SerializedName("productImage")
    private String base64Image;

    @SerializedName("salonId")
    private String salonId;

    @SerializedName("productId")
    private String productId;

    public String getOperation() {
        return operation;
    }

    public UpdateProductRequestModel setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public UpdateProductRequestModel setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public UpdateProductRequestModel setProductDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public UpdateProductRequestModel setProductPrice(String productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public UpdateProductRequestModel setBase64Image(String base64Image) {
        this.base64Image = base64Image;
        return this;
    }

    public String getSalonId() {
        return salonId;
    }

    public UpdateProductRequestModel setSalonId(String salonId) {
        this.salonId = salonId;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public UpdateProductRequestModel setProductId(String productId) {
        this.productId = productId;
        return this;
    }
}
