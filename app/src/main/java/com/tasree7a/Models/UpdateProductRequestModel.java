package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class UpdateProductRequestModel {

    @SerializedName("operation")
    String operation; // ADD/Remove/Update

    @SerializedName("productName")
    String productName;

    @SerializedName("productDescription")
    String productDescription;

    @SerializedName("productPrice")
    String productPrice;

    @SerializedName("productImage")
    String base64Image;

    @SerializedName("salonId")
    String salonId;

    @SerializedName("productId")
    String productId;


    public String getOperation() {

        return operation;
    }


    public void setOperation(String operation) {

        this.operation = operation;
    }


    public String getProductName() {

        return productName;
    }


    public void setProductName(String productName) {

        this.productName = productName;
    }


    public String getProductDescription() {

        return productDescription;
    }


    public void setProductDescription(String productDescription) {

        this.productDescription = productDescription;
    }


    public String getProductPrice() {

        return productPrice;
    }


    public void setProductPrice(String productPrice) {

        this.productPrice = productPrice;
    }


    public String getBase64Image() {

        return base64Image;
    }


    public void setBase64Image(String base64Image) {

        this.base64Image = base64Image;
    }


    public String getSalonId() {

        return salonId;
    }


    public void setSalonId(String id) {

        this.salonId = id;
    }


    public String getProductId() {

        return productId;
    }


    public void setProductId(String productId) {

        this.productId = productId;
    }
}
