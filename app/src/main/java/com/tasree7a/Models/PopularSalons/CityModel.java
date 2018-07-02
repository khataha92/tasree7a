package com.tasree7a.models.popularsalons;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 7/6/17.
 */

public class CityModel {

    @SerializedName("id")
    String id;

    @SerializedName("mName")
    String name;

//    @SerializedName("cLat")
    double lat;

//    @SerializedName("cLong")
    double lng;

    @SerializedName("country_id")
    CountryModel countryId;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public CountryModel getCountryId() {
        return countryId;
    }

    @Override
    public String toString() {
        return name;
    }
}
