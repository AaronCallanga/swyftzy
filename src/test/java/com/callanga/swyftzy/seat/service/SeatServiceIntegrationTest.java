package com.callanga.swyftzy.seat.service;

import com.callanga.swyftzy.BaseIntegrationTest;
import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.flight.repository.FlightRepository;
import com.callanga.swyftzy.seat.dto.SeatResponse;
import com.callanga.swyftzy.seat.entity.Seat;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatLocation;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import com.callanga.swyftzy.seat.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class SeatServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private SeatService seatService;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatRepository seatRepository;

    private UUID flightId;

    @BeforeEach
    void setUp() {
        Flight flight = Flight.builder()
                .flightNumber("ST-BR456")
                .origin("TPE")
                .destination("BKK")
                .departureTime(LocalDateTime.of(2026, 7, 1, 14, 0))
                .arrivalTime(LocalDateTime.of(2026, 7, 1, 17, 20))
                .aircraftType("Airbus A350-900")
                .status(FlightStatus.SCHEDULED)
                .build();
        flight = flightRepository.save(flight);
        flightId = flight.getId();

        seatRepository.save(new Seat(flight, "1A", CabinClass.BUSINESS, SeatLocation.WINDOW));
        seatRepository.save(new Seat(flight, "1B", CabinClass.BUSINESS, SeatLocation.MIDDLE));
        seatRepository.save(new Seat(flight, "1C", CabinClass.BUSINESS, SeatLocation.AISLE));
        seatRepository.save(new Seat(flight, "10A", CabinClass.ECONOMY, SeatLocation.WINDOW));
        seatRepository.save(new Seat(flight, "10B", CabinClass.ECONOMY, SeatLocation.MIDDLE));
        seatRepository.save(new Seat(flight, "10C", CabinClass.ECONOMY, SeatLocation.AISLE));
    }

    @Test
    void listSeats_withNoFilters_returnsAllSeats() {
        Page<SeatResponse> result = seatService.listSeats(flightId, null, null, null, PageRequest.of(0, 20));
        assertThat(result.getContent()).hasSize(6);
    }

    @Test
    void listSeats_withCabinFilter_returnsOnlyMatchingCabin() {
        Page<SeatResponse> result = seatService.listSeats(flightId, CabinClass.BUSINESS, null, null, PageRequest.of(0, 20));
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent()).allMatch(s -> s.cabin() == CabinClass.BUSINESS);
    }

    @Test
    void listSeats_withLocationFilter_returnsOnlyMatchingLocation() {
        Page<SeatResponse> result = seatService.listSeats(flightId, null, null, SeatLocation.WINDOW, PageRequest.of(0, 20));
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(s -> s.location() == SeatLocation.WINDOW);
    }

    @Test
    void listSeats_withCabinAndLocationFilter_returnsIntersection() {
        Page<SeatResponse> result = seatService.listSeats(flightId, CabinClass.ECONOMY, null, SeatLocation.AISLE, PageRequest.of(0, 20));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().seatNumber()).isEqualTo("10C");
    }

    @Test
    void listSeats_withStatusFilter_returnsOnlyMatchingStatus() {
        Seat bookedSeat = seatRepository.findByFlightIdAndSeatNumber(flightId, "1A").orElseThrow();
        bookedSeat.book();
        seatRepository.save(bookedSeat);

        Page<SeatResponse> result = seatService.listSeats(flightId, null, SeatStatus.BOOKED, null, PageRequest.of(0, 20));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().seatNumber()).isEqualTo("1A");
    }

    @Test
    void listSeats_withPagination_returnsCorrectPage() {
        Page<SeatResponse> page1 = seatService.listSeats(flightId, null, null, null, PageRequest.of(0, 2));
        Page<SeatResponse> page2 = seatService.listSeats(flightId, null, null, null, PageRequest.of(1, 2));

        assertThat(page1.getContent()).hasSize(2);
        assertThat(page2.getContent()).hasSize(2);
        assertThat(page1.getContent()).doesNotContainAnyElementsOf(page2.getContent());
    }
}
