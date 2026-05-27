package com.callanga.swyftzy.booking.dto;

import com.callanga.swyftzy.booking.enums.BookingStatus;

import java.time.Instant;

public record BookingCancellationResponse(
        String bookingReference,
        BookingStatus status,
        Instant cancelledAt
) {
}
