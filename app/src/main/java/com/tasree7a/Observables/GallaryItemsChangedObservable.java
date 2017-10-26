package com.tasree7a.Observables;

import com.tasree7a.Models.Gallery.ImageModel;

import java.util.List;
import java.util.Observable;

/**
 * Created by SamiKhleaf on 10/24/17.
 */

public class GallaryItemsChangedObservable extends Observable {


    private static volatile GallaryItemsChangedObservable instance = null;


    private GallaryItemsChangedObservable() {

    }


    public static synchronized GallaryItemsChangedObservable sharedInstance() {

        if (instance == null) {

            instance = new GallaryItemsChangedObservable();

        }

        return instance;
    }


    /**
     * Set has changed to true to be able to notify observers
     * @param changed
     */
    public void setGallaryChanged(List<ImageModel> changed) {

        setChanged();

        notifyObservers(changed);

    }

}
