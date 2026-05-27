package com.callanga.swyftzy.shared.exception;

import java.util.UUID;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(UUID flightId) {
        super("Flight not found with id: " + flightId);
    }
}
