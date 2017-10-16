package com.tasree7a.Models.SalonBooking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mac on 10/9/17.
 */

public class AvailableTimesResponse {

    @SerializedName("details")
    List<Integer> availableTimes;

    public List<Integer> getAvailableTimes() {
        return availableTimes;
    }
}
