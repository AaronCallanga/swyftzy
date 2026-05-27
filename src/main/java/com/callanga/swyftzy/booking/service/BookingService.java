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
import com.callanga.swyftzy.flight.repository.FlightRepository;
import com.callanga.swyftzy.flight.service.FlightService;
import com.callanga.swyftzy.seat.entity.Seat;
import com.callanga.swyftzy.seat.service.SeatService;
import com.callanga.swyftzy.shared.exception.BookingNotFoundException;
import com.callanga.swyftzy.shared.exception.FlightNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final BookingReferenceGenerator bookingReferenceGenerator;
    private final SeatService seatService;
    private final FlightService flightService;

    public BookingResponse bookSeat(BookingRequest bookingRequest) {
        Flight flight = flightService.getFlightById(bookingRequest.flightId());
        Seat seat = seatService.getSeatByFlightIdAndSeatNumberWithLock(
                bookingRequest.flightId(),
                bookingRequest.seatNumber()
                                                                      );
        seat.book();

        Booking booking = Booking.builder()
                                 .bookingReference(bookingReferenceGenerator.generate(flight, seat.getSeatNumber()))
                                 .flight(flight)
                                 .seat(seat)
                                 .passengerName(bookingRequest.passengerName())
                                 .passengerEmail(bookingRequest.passengerEmail())
                                 .build();

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(savedBooking);
    }

    public BookingCancellationResponse cancelBooking(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                                           .orElseThrow(() -> new BookingNotFoundException(bookingReference));

        if (booking.getStatus().isCancelled()) {
            return bookingMapper.toCancellationResponse(booking);
        }

        booking.cancel();
        booking.getSeat().cancel();

        return bookingMapper.toCancellationResponse(booking);
    }
}
