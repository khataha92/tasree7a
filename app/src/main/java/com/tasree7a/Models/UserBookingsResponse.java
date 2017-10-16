package com.tasree7a.Models;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Models.Bookings.BookingModel;

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
