package com.tasree7a.Observables;

import com.tasree7a.Models.Bookings.BookingModel;
import com.tasree7a.Models.SalonDetails.SalonModel;

import java.util.Observable;

/**
 * Created by samikhleaf on 2/24/18.
 */

public class BookingStatusChangedObservable extends Observable {

    private static volatile BookingStatusChangedObservable instance = null;

    private BookingStatusChangedObservable() {
    }

    public static synchronized BookingStatusChangedObservable sharedInstance() {

        if (instance == null) {

            instance = new BookingStatusChangedObservable();

        }

        return instance;
    }

    /**
     * Set has changed to true to be able to notify observers
     */
    public void setStatusChanged(BookingModel bookingModel) {

        setChanged();

        notifyObservers(bookingModel);

    }
}
