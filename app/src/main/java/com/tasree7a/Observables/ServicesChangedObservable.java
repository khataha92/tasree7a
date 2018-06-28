package com.tasree7a.observables;

import java.util.Observable;

/**
 * Created by samikhleaf on 2/24/18.
 */

public class ServicesChangedObservable extends Observable{

    private static volatile ServicesChangedObservable instance = null;


    private ServicesChangedObservable() {

    }


    public static synchronized ServicesChangedObservable sharedInstance() {

        if (instance == null) {

            instance = new ServicesChangedObservable();

        }

        return instance;
    }


    /**
     * Set has changed to true to be able to notify observers
     * @param
     */
    public void setServicesChanged() {

        setChanged();

        notifyObservers();

    }
}