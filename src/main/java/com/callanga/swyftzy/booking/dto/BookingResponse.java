package com.callanga.swyftzy.booking.dto;

import com.callanga.swyftzy.booking.enums.BookingStatus;

import java.time.Instant;

public record BookingResponse(
        String bookingReference,
        String flightNumber,
        String seatNumber,
        String passengerName,
        BookingStatus status,
        Instant bookedAt
) {
}
