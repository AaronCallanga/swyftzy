package com.callanga.swyftzy.shared.exception;

public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String seatNumber) {
        super("Seat " + seatNumber + " is already booked");
    }
}
