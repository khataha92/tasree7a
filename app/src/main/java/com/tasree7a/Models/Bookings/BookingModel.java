package com.tasree7a.Models.Bookings;

import java.util.List;

/**
 * Created by mac on 8/31/17.
 */

public class BookingModel {

    String bookingId;

    String salonName;

    String salonAddress;

    String bookingTime;

    List<BookingServiceModel> bookingServiceList;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSalonAddress() {
        return salonAddress;
    }

    public void setSalonAddress(String salonAddress) {
        this.salonAddress = salonAddress;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public List<BookingServiceModel> getBookingServiceList() {
        return bookingServiceList;
    }

    public void setBookingServiceList(List<BookingServiceModel> bookingServiceList) {
        this.bookingServiceList = bookingServiceList;
    }
}
