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

    List<String> selectedItems = new ArrayList<>();

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


    public void addSelectedItem(String id) {

        selectedItems.add(id);

    }


    public void removeSelectedItem(String id) {

        selectedItems.remove(id);
    }


    public List<String> getSelectedItems() {

        return selectedItems;
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


    public void setFbImage(String fbImage) {

        this.fbImage = fbImage;
    }


    public String getFbImage() {

        return fbImage;
    }
}

