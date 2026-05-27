package com.callanga.swyftzy.flight.service;

import com.callanga.swyftzy.flight.dto.FlightResponse;
import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.flight.mapper.FlightMapper;
import com.callanga.swyftzy.flight.repository.FlightRepository;
import com.callanga.swyftzy.flight.repository.filter.FlightFilter;
import com.callanga.swyftzy.flight.repository.specification.FlightSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    /**
     * Searches for flights matching the given criteria.
     * Supports multi-value destination filtering for flexible route searches.
     *
     * @param origin        the origin airport IATA code (required)
     * @param destinations  list of destination airport codes (optional)
     * @param departureDate the departure date (required)
     * @param pageable      pagination parameters
     * @return a page of matching flights
     */
    public Page<FlightResponse> searchFlights(
            String origin,
            List<String> destinations,
            LocalDate departureDate,
            Pageable pageable
                                             ) {

        FlightFilter.FlightFilterBuilder builder = FlightFilter.builder();

        if (origin != null && !origin.isBlank()) {
            builder.origin(origin);
        }

        if (destinations != null && !destinations.isEmpty()) {
            builder.destinations(destinations);
        }

        if (departureDate != null) {
            builder.startOfDay(departureDate.atStartOfDay());
            builder.endOfDay(departureDate.atTime(LocalTime.MAX));
        }

        FlightFilter filter = builder.build();

        Page<Flight> flights = flightRepository.findAll(
                FlightSpecification.withFilters(filter),
                pageable
                                                       );

        return flights.map(flightMapper::toResponse);
    }
}
