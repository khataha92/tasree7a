package com.tasree7a.interfaces;

import com.tasree7a.enums.BookingStatus;

public interface OnBookingStatusChangedListener {
    void onBookingStatusChanged(int position, BookingStatus status);
}
