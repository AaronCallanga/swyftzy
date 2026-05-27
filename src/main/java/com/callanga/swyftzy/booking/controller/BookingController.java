package com.callanga.swyftzy.booking.controller;

import com.callanga.swyftzy.booking.dto.BookingCancellationResponse;
import com.callanga.swyftzy.booking.dto.BookingRequest;
import com.callanga.swyftzy.booking.dto.BookingResponse;
import com.callanga.swyftzy.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @GetMapping("/flight/{flightId}")
    @Operation(
            summary = "List bookings by flight",
            description = "Retrieve all bookings for a specific flight with pagination support."
    )
    public ResponseEntity<Page<BookingResponse>> getBookingsByFlight(
            @PathVariable UUID flightId,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<BookingResponse> bookings = bookingService.getBookingsByFlight(flightId, pageable);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/flight/{flightId}/seat/{seatId}")
    @Operation(
            summary = "Get booking by flight and seat",
            description = "Retrieve the booking associated with a specific seat on a flight."
    )
    public ResponseEntity<BookingResponse> getBookingByFlightAndSeat(
            @PathVariable UUID flightId,
            @PathVariable UUID seatId) {

        return bookingService.getBookingByFlightAndSeat(flightId, seatId)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
    }
}
