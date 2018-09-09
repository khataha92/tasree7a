package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;

public class LocationResponseModel {

    @SerializedName("details")
    private String mAddress;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
