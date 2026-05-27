package com.callanga.swyftzy.shared.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String bookingReference) {
        super("Booking not found with reference: " + bookingReference);
    }
}
