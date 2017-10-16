package com.tasree7a.Models.Login;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Models.SalonDetails.SalonModel;

import java.util.List;

/**
 * Created by mac on 9/24/17.
 */

public class User {

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

    @SerializedName("is_business")
    boolean isBusiness;

    @SerializedName("facebook_id")
    String facebookId;

    @SerializedName("salon_id")
    String salongId;

    public String getSalongId() {
        return salongId;
    }

    public void setSalongId(String salongId) {
        this.salongId = salongId;
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
        return isBusiness;
    }

    public void setBusiness(boolean business) {
        isBusiness = business;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
