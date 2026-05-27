package com.callanga.swyftzy.booking.mapper;

import com.callanga.swyftzy.booking.dto.BookingCancellationResponse;
import com.callanga.swyftzy.booking.dto.BookingResponse;
import com.callanga.swyftzy.booking.entity.Booking;
import com.callanga.swyftzy.booking.enums.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getBookingReference(),
                booking.getFlight().getFlightNumber(),
                booking.getSeat().getSeatNumber(),
                booking.getPassengerName(),
                booking.getStatus(),
                booking.getBookedAt().toInstant(ZoneOffset.UTC)
        );
    }

    public BookingCancellationResponse toCancellationResponse(Booking booking) {
        return new BookingCancellationResponse(
                booking.getBookingReference(),
                booking.getStatus(),
                booking.getCancelledAt().toInstant(ZoneOffset.UTC)
        );
    }
}