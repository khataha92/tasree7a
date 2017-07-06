package com.tasree7a.Models.PopularSalons;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.Constants;
import com.tasree7a.Enums.FilterType;
import com.tasree7a.interfaces.Filterable;
import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by mac on 7/4/17.
 */

public class SalonModel implements Filterable {

    @SerializedName("rank")
    RankModel rank;

    @SerializedName("city")
    CityModel salonCity;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("latitude")
    double lat;

    @SerializedName("longitude")
    double lng;

    @SerializedName("owner_name")
    String ownerName;

    @SerializedName("owner_mobile")
    String ownerMobileNumber;

    @SerializedName("distance")
    double distance;

    @SerializedName("salon_img")
    String image;

    public String getImage() {
        return Constants.IMAGE_PREFIX+image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerMobileNumber() {
        return ownerMobileNumber;
    }

    public void setOwnerMobileNumber(String ownerMobileNumber) {
        this.ownerMobileNumber = ownerMobileNumber;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getSalonCity() {
        return salonCity.getName();
    }

    public CityModel getCityModel(){

        return salonCity;
    }

    public int getRating() {

        return (int)rank.getRank();

    }

    @Override
    public boolean filterValue(FilterType filterType) {

        switch (filterType){

            case ALL:

                return true;

            case AVAILABLE:

                return true;

            case FAVORITE:

                return UserDefaultUtil.isSalonFavorite(this);

        }

        return false;

    }
}
