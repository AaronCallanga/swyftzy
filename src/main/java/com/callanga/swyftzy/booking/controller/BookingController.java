package com.callanga.swyftzy.booking.controller;

import com.callanga.swyftzy.booking.dto.BookingCancellationResponse;
import com.callanga.swyftzy.booking.dto.BookingRequest;
import com.callanga.swyftzy.booking.dto.BookingResponse;
import com.callanga.swyftzy.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Seat reservation and cancellation endpoints")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(
            summary = "Reserve a seat",
            description = "Book a specific seat on a flight. Uses pessimistic locking to prevent double-booking."
    )
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest request) {

        BookingResponse response = bookingService.bookSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{bookingReference}")
    @Operation(
            summary = "Cancel a booking",
            description = "Cancel an existing booking by its reference and release the seat."
    )
    public ResponseEntity<BookingCancellationResponse> cancelBooking(@PathVariable String bookingReference) {

        BookingCancellationResponse response = bookingService.cancelBooking(bookingReference);
        return ResponseEntity.ok(response);
    }
}
