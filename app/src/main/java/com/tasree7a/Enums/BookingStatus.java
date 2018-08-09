package com.tasree7a.enums;

public enum BookingStatus {
    CANCELED_BY_BARBER(6),
    CREATED(1),
    ACCEPTED(2),
    REJECTED(3),
    CANCELED_BY_USER(4),
    FINISHED(5);

    public int value;

    BookingStatus(int val) {
        this.value = val;
    }
}
