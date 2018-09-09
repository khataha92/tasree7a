package com.tasree7a.models.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by mac on 9/24/17.
 */

public class User implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("user_id")
    String userId;

    @SerializedName("email")
    String email;

    @SerializedName("phone_no")
    String phoneNumber;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("isActive")
    int isActive;

    @SerializedName("gender")
    String gender;

    @SerializedName("user_type")
    String userType;

    @SerializedName("image")
    String imageUrl;

    @SerializedName("fb_flag")
    boolean isFacebook;

    @SerializedName("is_buisness")
    int isBusiness;

    @SerializedName("facebook_id")
    String facebookId;

    @SerializedName("salon_id")
    String salonId;

    @SerializedName("user_auth_token")
    private String mUserToken;

    public String getSalonId () {

        return salonId;
    }


    public void setSalonId (String salonId) {

        this.salonId = salonId;
    }


    public String getId() {

        return id;
    }


    public void setId(String id) {

        this.id = id;
    }


    public String getUserId() {

        return userId;
    }


    public void setUserId(String userId) {

        this.userId = userId;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }


    public String getPhoneNumber() {

        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }


    public String getFirstName() {

        return firstName;
    }


    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }


    public String getLastName() {

        return lastName;
    }


    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    public boolean isActive() {

        return isActive == 1;
    }


    public void setActive(boolean active) {

        isActive = active ? 1 : 0;
    }


    public String getGender() {

        return gender;
    }


    public void setGender(String gender) {

        this.gender = gender;
    }


    public String getUserType() {

        return userType;
    }


    public void setUserType(String userType) {

        this.userType = userType;
    }


    public String getImageUrl() {

        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }


    public boolean isFacebook() {

        return isFacebook;
    }


    public void setFacebook(boolean facebook) {

        isFacebook = facebook;
    }


    public boolean isBusiness() {

        return isBusiness == 1;
    }


    public void setBusiness(int business) {

        isBusiness = business;
    }


    public String getFacebookId() {

        return facebookId;
    }


    public void setFacebookId(String facebookId) {

        this.facebookId = facebookId;
    }

    public String getUserToken() {
        return mUserToken;
    }

    public User setUserToken(String userToken) {
        mUserToken = userToken;
        return this;
    }
}
