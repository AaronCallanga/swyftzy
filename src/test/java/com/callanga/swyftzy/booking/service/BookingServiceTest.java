package com.callanga.swyftzy.booking.service;

import com.callanga.swyftzy.booking.dto.BookingCancellationResponse;
import com.callanga.swyftzy.booking.dto.BookingRequest;
import com.callanga.swyftzy.booking.dto.BookingResponse;
import com.callanga.swyftzy.booking.entity.Booking;
import com.callanga.swyftzy.booking.enums.BookingStatus;
import com.callanga.swyftzy.booking.mapper.BookingMapper;
import com.callanga.swyftzy.booking.repository.BookingRepository;
import com.callanga.swyftzy.booking.util.BookingReferenceGenerator;
import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.flight.service.FlightService;
import com.callanga.swyftzy.seat.entity.Seat;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatLocation;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import com.callanga.swyftzy.seat.service.SeatService;
import com.callanga.swyftzy.shared.exception.BookingNotFoundException;
import com.callanga.swyftzy.shared.exception.SeatAlreadyBookedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingReferenceGenerator bookingReferenceGenerator;

    @Mock
    private SeatService seatService;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void bookSeat_whenSeatAlreadyBooked_throwsSeatAlreadyBookedException() {
        UUID flightId = UUID.randomUUID();
        Flight flight = Flight.builder()
                .id(flightId)
                .flightNumber("BR123")
                .origin("TPE")
                .destination("HKG")
                .departureTime(LocalDateTime.of(2026, 6, 15, 8, 30))
                .arrivalTime(LocalDateTime.of(2026, 6, 15, 10, 45))
                .aircraftType("Boeing 777-300ER")
                .status(FlightStatus.SCHEDULED)
                .build();

        Seat seat = new Seat(flight, "12A", CabinClass.BUSINESS, SeatLocation.WINDOW);
        seat.book();

        BookingRequest request = new BookingRequest(flightId, "12A", "John Doe", "john@example.com");

        when(flightService.getFlightById(flightId)).thenReturn(flight);
        when(seatService.getSeatByFlightIdAndSeatNumberWithLock(flightId, "12A")).thenReturn(seat);

        assertThatThrownBy(() -> bookingService.bookSeat(request))
                .isInstanceOf(SeatAlreadyBookedException.class)
                .hasMessageContaining("12A");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void cancelBooking_whenBookingDoesNotExist_throwsBookingNotFoundException() {
        String reference = "NONEXISTENT";
        when(bookingRepository.findByBookingReference(reference)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(reference))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining(reference);
    }

    @Test
    void cancelBooking_whenAlreadyCancelled_returnsCancellationResponseWithoutDoubleCancel() {
        String reference = "ABC123";
        UUID flightId = UUID.randomUUID();

        Flight flight = Flight.builder()
                .id(flightId)
                .flightNumber("BR123")
                .origin("TPE")
                .destination("HKG")
                .departureTime(LocalDateTime.of(2026, 6, 15, 8, 30))
                .arrivalTime(LocalDateTime.of(2026, 6, 15, 10, 45))
                .aircraftType("Boeing 777-300ER")
                .status(FlightStatus.SCHEDULED)
                .build();

        Seat seat = spy(new Seat(flight, "12A", CabinClass.BUSINESS, SeatLocation.WINDOW));
        seat.book();

        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .bookingReference(reference)
                .flight(flight)
                .seat(seat)
                .passengerName("John Doe")
                .passengerEmail("john@example.com")
                .status(BookingStatus.CANCELLED)
                .bookedAt(LocalDateTime.now())
                .cancelledAt(LocalDateTime.now())
                .build();

        BookingCancellationResponse response = new BookingCancellationResponse(reference, BookingStatus.CANCELLED, Instant.now());

        when(bookingRepository.findByBookingReference(reference)).thenReturn(Optional.of(booking));
        when(bookingMapper.toCancellationResponse(booking)).thenReturn(response);

        BookingCancellationResponse result = bookingService.cancelBooking(reference);

        assertThat(result).isEqualTo(response);
        verify(seat, never()).cancel();
    }
}
