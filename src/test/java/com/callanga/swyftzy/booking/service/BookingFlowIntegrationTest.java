package com.callanga.swyftzy.booking.service;

import com.callanga.swyftzy.BaseIntegrationTest;
import com.callanga.swyftzy.booking.dto.BookingCancellationResponse;
import com.callanga.swyftzy.booking.dto.BookingRequest;
import com.callanga.swyftzy.booking.dto.BookingResponse;
import com.callanga.swyftzy.booking.enums.BookingStatus;
import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.flight.repository.FlightRepository;
import com.callanga.swyftzy.seat.entity.Seat;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatLocation;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import com.callanga.swyftzy.seat.repository.SeatRepository;
import com.callanga.swyftzy.shared.exception.BookingNotFoundException;
import com.callanga.swyftzy.shared.exception.SeatAlreadyBookedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class BookingFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatRepository seatRepository;

    private UUID flightId;
    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = Flight.builder()
                .flightNumber("IT-BR123")
                .origin("TPE")
                .destination("HKG")
                .departureTime(LocalDateTime.of(2026, 7, 1, 8, 30))
                .arrivalTime(LocalDateTime.of(2026, 7, 1, 10, 45))
                .aircraftType("Boeing 777-300ER")
                .status(FlightStatus.SCHEDULED)
                .build();
        flight = flightRepository.save(flight);
        flightId = flight.getId();
    }

    @Test
    void bookSeat_happyPath_createsConfirmedBooking() {
        Seat seat = new Seat(flight, "12A", CabinClass.ECONOMY, SeatLocation.WINDOW);
        seatRepository.save(seat);

        BookingRequest request = new BookingRequest(flightId, "12A", "Alice", "alice@test.com");
        BookingResponse response = bookingService.bookSeat(request);

        assertThat(response).isNotNull();
        assertThat(response.bookingReference()).isNotBlank();
        assertThat(response.status()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(response.passengerName()).isEqualTo("Alice");

        Seat updatedSeat = seatRepository.findByFlightIdAndSeatNumber(flightId, "12A").orElseThrow();
        assertThat(updatedSeat.getStatus()).isEqualTo(SeatStatus.BOOKED);
    }

    @Test
    void bookSeat_whenSeatAlreadyBooked_throwsException() {
        Seat seat = new Seat(flight, "12A", CabinClass.ECONOMY, SeatLocation.AISLE);
        Seat managedSeat = seatRepository.save(seat);
        managedSeat.book();
        seatRepository.save(managedSeat);

        BookingRequest request = new BookingRequest(flightId, "12A", "Bob", "bob@test.com");

        assertThatThrownBy(() -> bookingService.bookSeat(request))
                .isInstanceOf(SeatAlreadyBookedException.class)
                .hasMessageContaining("12A");
    }

    @Test
    void cancelBooking_releasesSeat() {
        Seat seat = new Seat(flight, "14B", CabinClass.BUSINESS, SeatLocation.WINDOW);
        seatRepository.save(seat);

        BookingRequest request = new BookingRequest(flightId, "14B", "Carol", "carol@test.com");
        BookingResponse booking = bookingService.bookSeat(request);

        BookingCancellationResponse cancelResponse = bookingService.cancelBooking(booking.bookingReference());

        assertThat(cancelResponse.status()).isEqualTo(BookingStatus.CANCELLED);

        Seat updatedSeat = seatRepository.findByFlightIdAndSeatNumber(flightId, "14B").orElseThrow();
        assertThat(updatedSeat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);
    }

    @Test
    void cancelBooking_whenAlreadyCancelled_doesNotThrow() {
        Seat seat = new Seat(flight, "16C", CabinClass.ECONOMY, SeatLocation.MIDDLE);
        seatRepository.save(seat);

        BookingRequest request = new BookingRequest(flightId, "16C", "Dave", "dave@test.com");
        BookingResponse booking = bookingService.bookSeat(request);

        bookingService.cancelBooking(booking.bookingReference());
        BookingCancellationResponse response = bookingService.cancelBooking(booking.bookingReference());

        assertThat(response.status()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void cancelBooking_whenNotFound_throwsException() {
        assertThatThrownBy(() -> bookingService.cancelBooking("DOESNOTEXIST"))
                .isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    void bookSeat_differentSeatsOnSameFlight_succeedIndependently() {
        Seat seatA = new Seat(flight, "1A", CabinClass.BUSINESS, SeatLocation.WINDOW);
        Seat seatB = new Seat(flight, "1B", CabinClass.BUSINESS, SeatLocation.MIDDLE);
        seatRepository.save(seatA);
        seatRepository.save(seatB);

        BookingResponse bookingA = bookingService.bookSeat(
                new BookingRequest(flightId, "1A", "Emma", "emma@test.com"));
        BookingResponse bookingB = bookingService.bookSeat(
                new BookingRequest(flightId, "1B", "Frank", "frank@test.com"));

        assertThat(bookingA.status()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(bookingB.status()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(bookingA.bookingReference()).isNotEqualTo(bookingB.bookingReference());
    }
}
