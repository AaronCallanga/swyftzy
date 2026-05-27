package com.callanga.swyftzy.flight.mapper;

import com.callanga.swyftzy.flight.dto.FlightResponse;
import com.callanga.swyftzy.flight.entity.Flight;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper {
    public FlightResponse toResponse(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAircraftType(),
                flight.getStatus()
        );
    }
}