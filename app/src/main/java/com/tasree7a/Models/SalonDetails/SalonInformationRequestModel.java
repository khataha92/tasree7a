package com.tasree7a.models.salondetails;

/**
 * Created by SamiKhleaf on 10/21/17.
 */

public class SalonInformationRequestModel {

    private String salonId;
    private String userID;
    private String cityID;
    private String salonType;
    private String salonBase64Image;
    private String ownerName;
    private String ownerMobile;
    private String salonLat;
    private String salonLong;
    private String salonName;
    private String userEmail;

    public String getSalonId() {
        return salonId;
    }

    public SalonInformationRequestModel setSalonId(String salonId) {
        this.salonId = salonId;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public SalonInformationRequestModel setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getUserID() {
        return userID;
    }

    public SalonInformationRequestModel setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getCityID() {
        return cityID;
    }

    public SalonInformationRequestModel setCityID(String cityID) {
        this.cityID = cityID;
        return this;
    }

    public String getSalonType() {
        return salonType;
    }

    public SalonInformationRequestModel setSalonType(String salonType) {
        this.salonType = salonType;
        return this;
    }

    public String getSalonBase64Image() {
        return salonBase64Image;
    }

    public SalonInformationRequestModel setSalonBase64Image(String salonBase64Image) {
        this.salonBase64Image = salonBase64Image;
        return this;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public SalonInformationRequestModel setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public SalonInformationRequestModel setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
        return this;
    }

    public String getSalonLat() {
        return salonLat;
    }

    public SalonInformationRequestModel setSalonLat(String salonLat) {
        this.salonLat = salonLat;
        return this;
    }

    public String getSalonLong() {
        return salonLong;
    }

    public SalonInformationRequestModel setSalonLong(String salonLong) {
        this.salonLong = salonLong;
        return this;
    }

    public String getSalonName() {
        return salonName;
    }

    public SalonInformationRequestModel setSalonName(String salonName) {
        this.salonName = salonName;
        return this;
    }
}
