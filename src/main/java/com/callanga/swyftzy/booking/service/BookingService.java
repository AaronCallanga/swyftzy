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
import com.callanga.swyftzy.flight.service.FlightService;
import com.callanga.swyftzy.seat.entity.Seat;
import com.callanga.swyftzy.seat.service.SeatService;
import com.callanga.swyftzy.shared.exception.BookingNotFoundException;
import com.callanga.swyftzy.shared.exception.FlightNotFoundException;
import com.callanga.swyftzy.shared.exception.SeatAlreadyBookedException;
import com.callanga.swyftzy.shared.exception.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final BookingReferenceGenerator bookingReferenceGenerator;
    private final SeatService seatService;
    private final FlightService flightService;

    /**
     * Reserves a seat on a flight using pessimistic locking to prevent double-booking.
     *
     * @param bookingRequest the booking request containing flight ID, seat number, and passenger details
     * @return the booking confirmation response
     * @throws FlightNotFoundException    if the flight does not exist
     * @throws SeatNotFoundException      if the seat does not exist on the flight
     * @throws SeatAlreadyBookedException if the seat is already booked
     */
    @Transactional
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
                                 .status(BookingStatus.CONFIRMED)
                                 .bookedAt(LocalDateTime.now())
                                 .build();

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(savedBooking);
    }

    @Transactional
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

    public Page<BookingResponse> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                                .map(bookingMapper::toResponse);
    }

    public Page<BookingResponse> getBookingsByFlight(UUID flightId, Pageable pageable) {
        return bookingRepository.findByFlightId(flightId, pageable)
                                .map(bookingMapper::toResponse);
    }

    public Optional<BookingResponse> getBookingByFlightAndSeat(UUID flightId, UUID seatId) {
        return bookingRepository.findByFlightIdAndSeatId(flightId, seatId)
                                .map(bookingMapper::toResponse);
    }

    public Optional<BookingResponse> getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference)
                                .map(bookingMapper::toResponse);
    }
}
