package com.callanga.swyftzy.booking.enums;

public enum BookingStatus {
    CONFIRMED,
    CANCELLED;

    public boolean isCancelled() {
        return this == CANCELLED;
    }
}
