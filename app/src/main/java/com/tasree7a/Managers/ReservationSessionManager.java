package com.tasree7a.Managers;

import com.tasree7a.Models.SalonBooking.SalonService;
import com.tasree7a.Models.SalonDetails.SalonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 9/17/17.
 */

public class ReservationSessionManager {

    private static ReservationSessionManager instance;

    private SalonModel salonModel = null;

    List<SalonService> selectedServices = new ArrayList<>();

    double total;

    private ReservationSessionManager(){


    }


    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public static ReservationSessionManager getInstance() {

        if(instance == null){

            instance = new ReservationSessionManager();

        }

        return instance;
    }

    public void setSelectedServices(List<SalonService> selectedServices) {
        this.selectedServices = selectedServices;
    }

    public void setSalonModel(SalonModel salonModel) {
        this.salonModel = salonModel;
    }

    public SalonModel getSalonModel() {
        return salonModel;
    }

    public List<SalonService> getSelectedServices() {
        return selectedServices;
    }
}

