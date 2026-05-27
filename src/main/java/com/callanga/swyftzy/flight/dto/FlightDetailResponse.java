package com.callanga.swyftzy.flight.dto;

import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.seat.dto.SeatAvailabilityResponse;
import com.callanga.swyftzy.seat.enums.CabinClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FlightDetailResponse(
        UUID id,
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        String aircraftType,
        FlightStatus status,
        List<SeatAvailabilityResponse> availability
) {}
