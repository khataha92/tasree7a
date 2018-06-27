package com.tasree7a.models.salondetails;

/**
 * Created by SamiKhleaf on 10/21/17.
 */

public class SalonInformationRequestModel {

    String userID;
    String cityID;
    String salonType;
    String salonBase64Image;
    String ownerName;
    String ownerMobile;
    String salonLat;
    String salonLong;
    String salonName;


    public String getUserID() {

        return userID;
    }


    public void setUserID(String userID) {

        this.userID = userID;
    }


    public String getCityID() {

        return cityID;
    }


    public void setCityID(String cityID) {

        this.cityID = cityID;
    }


    public String getSalonType() {

        return salonType;
    }


    public void setSalonType(String salonType) {

        this.salonType = salonType;
    }


    public String getSalonBase64Image() {

        return salonBase64Image;
    }


    public void setSalonBase64Image(String salonBase64Image) {

        this.salonBase64Image = salonBase64Image;
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


    public String getSalonLat() {

        return salonLat;
    }


    public void setSalonLat(String salonLat) {

        this.salonLat = salonLat;
    }


    public String getSalonLong() {

        return salonLong;
    }


    public void setSalonLong(String salonLong) {

        this.salonLong = salonLong;
    }


    public String getSalonName() {

        return salonName;
    }


    public void setSalonName(String salonName) {

        this.salonName = salonName;
    }
}
