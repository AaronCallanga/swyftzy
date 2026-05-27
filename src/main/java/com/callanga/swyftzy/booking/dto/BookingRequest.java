package com.callanga.swyftzy.booking.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;
public record BookingRequest(
        @NotNull(message = "Flight ID is required")
        UUID flightId,

        @NotBlank(message = "Seat number is required")
        @Size(min = 2, max = 4, message = "Seat number must be 2-4 characters")
        String seatNumber,

        @NotBlank(message = "Passenger name is required")
        @Size(max = 100, message = "Passenger name must not exceed 100 characters")
        String passengerName,

        @NotBlank(message = "Passenger email is required")
        @Email(message = "Passenger email must be a valid email address")
        @Size(max = 100, message = "Passenger email must not exceed 100 characters")
        String passengerEmail
) {
}
