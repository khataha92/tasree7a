package com.tasree7a.Models.SalonDetails;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 9/17/17.
 */

public class SalonBarber {

    @SerializedName("id")
    String barberId;

    @SerializedName("first_name")
    String barberFirstName;

    @SerializedName("last_name")
    String barberLastName;

    @SerializedName("start_time")
    int startTime;

    @SerializedName("end_time")
    int endTime;

    @SerializedName("email")
    String email;

    public String getEmail() {
        return email;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public String getBarberId() {
        return barberId;
    }

    public String getBarberFirstName() {
        return barberFirstName;
    }

    public String getBarberLastName() {
        return barberLastName;
    }
}
