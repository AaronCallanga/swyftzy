package com.callanga.swyftzy.flight.service;

import com.callanga.swyftzy.flight.dto.FlightDetailResponse;
import com.callanga.swyftzy.flight.dto.FlightResponse;
import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.flight.mapper.FlightMapper;
import com.callanga.swyftzy.flight.repository.FlightRepository;
import com.callanga.swyftzy.flight.repository.filter.FlightFilter;
import com.callanga.swyftzy.flight.repository.specification.FlightSpecification;
import com.callanga.swyftzy.seat.dto.SeatAvailabilityResponse;
import com.callanga.swyftzy.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final SeatService seatService;
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
            FlightFilter filter,
            Pageable pageable
                                             ) {

        Page<Flight> flights = flightRepository.findAll(
                FlightSpecification.withFilters(filter),
                pageable
                                                       );

        return flights.map(flightMapper::toResponse);
    }

    /**
     * Finds details of a specific flight
     *
     * @param id the flight UUID
     * @return an Optional containing the flight response if found
     */
    public Optional<FlightDetailResponse> findDetailedById(UUID id) {

        return flightRepository.findById(id)
                               .map(flight -> {

                                   List<SeatAvailabilityResponse> availability =
                                           seatService.getAvailability(flight.getId());

                                   return new FlightDetailResponse(
                                           flight.getId(),
                                           flight.getFlightNumber(),
                                           flight.getOrigin(),
                                           flight.getDestination(),
                                           flight.getDepartureTime(),
                                           flight.getArrivalTime(),
                                           flight.getAircraftType(),
                                           flight.getStatus(),
                                           availability
                                   );
                               });
    }
}
