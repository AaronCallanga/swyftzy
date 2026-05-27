package com.callanga.swyftzy.shared.exception;

import java.util.UUID;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(UUID seatId) {
        super("Seat not found with id: " + seatId);
    }
    public SeatNotFoundException(String message) {
        super(message);
    }
}
