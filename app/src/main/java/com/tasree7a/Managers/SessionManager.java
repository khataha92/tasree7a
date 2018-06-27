package com.tasree7a.managers;

import com.tasree7a.models.salondetails.SalonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 7/6/17.
 */

public class SessionManager {

    List<SalonModel> salons = new ArrayList<>();

    private static SessionManager instance = null;

    private SessionManager(){

    }

    public static SessionManager getInstance() {

        if(instance == null){

            instance = new SessionManager();

        }

        return instance;
    }

    public void setSalons(List<SalonModel> salons) {

        this.salons = salons;

    }

    public List<SalonModel> getSalons() {
        return salons;
    }
}
