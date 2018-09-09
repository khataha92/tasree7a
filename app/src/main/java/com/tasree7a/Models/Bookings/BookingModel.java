package com.tasree7a.models.bookings;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.models.login.User;
import com.tasree7a.models.popularsalons.CityModel;
import com.tasree7a.models.popularsalons.CountryModel;
import com.tasree7a.models.salondetails.SalonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 8/31/17.
 */

public class BookingModel {

    @SerializedName("book_id")
    String bookingId;

    @SerializedName("salon")
    SalonModel salon;

    @SerializedName("book_time")
    String bookingTime;

    @SerializedName("book_status")
    int bookStatus;

    @SerializedName("book_date")
    String bookDate;

    @SerializedName("city")
    CityModel city;

    @SerializedName("country")
    CountryModel country;

    @SerializedName("user")
    User user;

    @SerializedName("start_time")
    String startTime;

    @SerializedName("end_time")
    String endTime;

    @SerializedName("services")
    List<BookingServiceModel> bookingServiceList = new ArrayList<>();

    public String getBookDate() {
        return bookDate;
    }

    public BookingModel setBookDate(String bookDate) {
        this.bookDate = bookDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public BookingModel setUser(User user) {
        this.user = user;
        return this;
    }

    public String getBookingId() {
        return bookingId;
    }

    public int getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(int bookStatus) {
        this.bookStatus = bookStatus;
    }

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public CountryModel getCountry() {
        return country;
    }

    public void setCountry(CountryModel country) {
        this.country = country;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSalonName() {

        if (salon != null) {

            return salon.getName();
        }

        return "";
    }

    public String getSalonAddress() {

        String countryName = "";

        String cityName = "";

        String address = "";

        if (city != null) {

            cityName = city.getName();


            address = cityName;
        }

        if (country != null) {

            countryName = country.getName();

            address += ", " + countryName;

        }

        return address;
    }


    public SalonModel getSalon() {

        return salon;
    }


    public void setSalon(SalonModel salon) {

        this.salon = salon;
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

    public String getStartTime() {
        return startTime;
    }

    public BookingModel setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public BookingModel setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }
}
