package com.tasree7a.observables;

import java.util.Observable;

/**
 * Created by SamiKhleaf on 10/20/17.
 */

public class ItemSelectedObservable extends Observable {


    private static volatile ItemSelectedObservable instance = null;


    private ItemSelectedObservable() {

    }


    public static synchronized ItemSelectedObservable sharedInstance() {

        if (instance == null) {

            instance = new ItemSelectedObservable();

        }

        return instance;
    }


    /**
     * Set has changed to true to be able to notify observers
     */
    public void setItemSelected(boolean selected) {

        setChanged();

        notifyObservers(selected);

    }

}
