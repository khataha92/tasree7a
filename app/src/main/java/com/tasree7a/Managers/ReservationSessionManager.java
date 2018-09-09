package com.tasree7a.managers;

import com.tasree7a.models.salondetails.SalonModel;

/**
 * Created by mac on 9/17/17.
 */

public class ReservationSessionManager {

    private static ReservationSessionManager instance;

    private SalonModel salonModel = null;

    double total;

    String fbImage;

    private ReservationSessionManager() {
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public static ReservationSessionManager getInstance() {
        if (instance == null) {
            instance = new ReservationSessionManager();
        }
        return instance;
    }

    public void setSalonModel(SalonModel salonModel) {
        this.salonModel = salonModel;
    }

    public SalonModel getSalonModel() {
        return salonModel;
    }

    public void setFbImage(String fbImage) {
        this.fbImage = fbImage;
    }

    public String getFbImage() {
        return fbImage;
    }
}