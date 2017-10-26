package com.tasree7a.Models.SalonDetails;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class CreateSalonResponseDetails {

    @SerializedName("name")
    String salonName;

    @SerializedName("latitude")
    String lat;

    @SerializedName("longitude")
    String longi;

    @SerializedName("owner_name")
    String ownerName;

    @SerializedName("owner_mobile")
    String ownerMobile;

    @SerializedName("salon_img")
    String salonImage;

    @SerializedName("salon_type")
    String type;

    @SerializedName("owner_id")
    String ownerID;

    @SerializedName("id")
    String salonId;


    public String getSalonName() {

        return salonName;
    }


    public void setSalonName(String salonName) {

        this.salonName = salonName;
    }


    public String getLat() {

        return lat;
    }


    public void setLat(String lat) {

        this.lat = lat;
    }


    public String getLongi() {

        return longi;
    }


    public void setLongi(String longi) {

        this.longi = longi;
    }


    public String getOwnerName() {

        return ownerName;
    }


    public void setOwnerName(String ownerName) {

        this.ownerName = ownerName;
    }


    public String getOwnerMobile() {

        return ownerMobile;
    }


    public void setOwnerMobile(String ownerMobile) {

        this.ownerMobile = ownerMobile;
    }


    public String getSalonImage() {

        return salonImage;
    }


    public void setSalonImage(String salonImage) {

        this.salonImage = salonImage;
    }


    public String getType() {

        return type;
    }


    public void setType(String type) {

        this.type = type;
    }


    public String getOwnerID() {

        return ownerID;
    }


    public void setOwnerID(String ownerID) {

        this.ownerID = ownerID;
    }


    public String getSalonId() {

        return salonId;
    }


    public void setSalonId(String salonId) {

        this.salonId = salonId;
    }
}
