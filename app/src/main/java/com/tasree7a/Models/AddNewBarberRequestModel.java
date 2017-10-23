package com.tasree7a.Models;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class AddNewBarberRequestModel {

    @SerializedName("salonId")
    String salonId;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("email")
    String email;

    @SerializedName("username")
    String userName;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("password")
    String pass;

    @SerializedName("updated_at")
    String updatedAt;

    @SerializedName("startTime")
    String startTime;

    @SerializedName("endTime")
    String endTime;


    public String getSalonId() {

        return salonId;
    }


    public void setSalonId(String salonId) {

        this.salonId = salonId;
    }


    public String getLastName() {

        return lastName;
    }


    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }


    public String getUserName() {

        return userName;
    }


    public void setUserName(String userName) {

        this.userName = userName;
    }


    public String getFirstName() {

        return firstName;
    }


    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }


    public String getCreatedAt() {

        return createdAt;
    }


    public void setCreatedAt(String createdAt) {

        this.createdAt = createdAt;
    }


    public String getPass() {

        return pass;
    }


    public void setPass(String pass) {

        this.pass = pass;
    }


    public String getUpdatedAt() {

        return updatedAt;
    }


    public void setUpdatedAt(String updatedAt) {

        this.updatedAt = updatedAt;
    }


    public String getStartTime() {

        return startTime;
    }


    public void setStartTime(String startTime) {

        this.startTime = startTime;
    }


    public String getEndTime() {

        return endTime;
    }


    public void setEndTime(String endTime) {

        this.endTime = endTime;
    }
}
