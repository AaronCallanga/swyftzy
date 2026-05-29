package com.callanga.swyftzy.seat.entity;

import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatLocation;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import com.callanga.swyftzy.shared.exception.InvalidSeatStateException;
import com.callanga.swyftzy.shared.exception.SeatAlreadyBookedException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeatTest {

    private Flight createFlight() {
        return Flight.builder()
                .flightNumber("BR123")
                .origin("TPE")
                .destination("HKG")
                .departureTime(LocalDateTime.of(2026, 6, 15, 8, 30))
                .arrivalTime(LocalDateTime.of(2026, 6, 15, 10, 45))
                .aircraftType("Boeing 777-300ER")
                .status(FlightStatus.SCHEDULED)
                .build();
    }

    @Test
    void book_whenAlreadyBooked_throwsSeatAlreadyBookedException() {
        Flight flight = createFlight();
        Seat seat = new Seat(flight, "12A", CabinClass.BUSINESS, SeatLocation.WINDOW);

        seat.book();
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.BOOKED);

        assertThatThrownBy(seat::book)
                .isInstanceOf(SeatAlreadyBookedException.class)
                .hasMessageContaining("12A");
    }

    @Test
    void cancel_whenNotBooked_throwsInvalidSeatStateException() {
        Flight flight = createFlight();
        Seat seat = new Seat(flight, "12A", CabinClass.BUSINESS, SeatLocation.WINDOW);

        assertThat(seat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);

        assertThatThrownBy(seat::cancel)
                .isInstanceOf(InvalidSeatStateException.class)
                .hasMessageContaining("not booked");
    }
}
