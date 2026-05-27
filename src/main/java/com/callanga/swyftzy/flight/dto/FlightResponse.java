package com.callanga.swyftzy.flight.dto;

import com.callanga.swyftzy.flight.enums.FlightStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record FlightResponse(
        UUID id,
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        String aircraftType,
        FlightStatus status
) { }
