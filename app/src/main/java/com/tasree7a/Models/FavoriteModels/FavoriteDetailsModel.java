package com.tasree7a.models.favoritemodels;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.models.salondetails.SalonModel;

/**
 * Created by SamiKhleaf on 8/6/17.
 */

public class FavoriteDetailsModel {

    @SerializedName("user_id")
    int userId;


    @SerializedName("sallon_id")
    int salonId;


    @SerializedName("salon")
    SalonModel salonModel;


    public int getUserId() {

        return userId;
    }


    public void setUserId(int userId) {

        this.userId = userId;
    }


    public int getSalonId() {

        return salonId;
    }


    public void setSalonId(int salonId) {

        this.salonId = salonId;
    }


    public SalonModel getSalonModel() {

        return salonModel;
    }


    public void setSalonModel(SalonModel salonModel) {

        this.salonModel = salonModel;
    }
}
