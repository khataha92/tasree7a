package com.tasree7a.Models.PopularSalons;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 8/6/17.
 */

public class CountryModel {

    @SerializedName("id")
    String id;

    @SerializedName("country_name")
    String name;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
