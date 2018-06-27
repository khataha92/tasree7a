package com.tasree7a.models;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.models.bookings.BookingModel;

import java.util.List;

/**
 * Created by mac on 9/19/17.
 */

public class UserBookingsResponse {

    @SerializedName("details")
    List<BookingModel> userBookings ;


    public List<BookingModel> getUserBookings() {
        return userBookings;
    }
}
