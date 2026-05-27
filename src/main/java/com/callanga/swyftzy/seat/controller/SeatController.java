package com.callanga.swyftzy.seat.controller;

import com.callanga.swyftzy.seat.dto.SeatAvailabilityResponse;
import com.callanga.swyftzy.seat.dto.SeatResponse;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import com.callanga.swyftzy.seat.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flights/{flightId}/seats")
@RequiredArgsConstructor
@Tag(name = "Seats", description = "Seat listing and availability endpoints")
public class SeatController {
    private final SeatService seatService;

    @GetMapping
    @Operation(
            summary = "List seats for a flight",
            description = "List seats with optional cabin and status filters."
    )
    public ResponseEntity<Page<SeatResponse>> listSeats(
            @PathVariable UUID flightId,
            @RequestParam(required = false) CabinClass cabin,
            @RequestParam(required = false) SeatStatus status,
            @PageableDefault(size = 20, sort = "seatNumber") Pageable pageable
                                                       ) {
        return ResponseEntity.ok(seatService.listSeats(flightId, cabin, status, pageable));
    }

    @GetMapping("/availability")
    @Operation(
            summary = "Get seat availability",
            description = "Get the total and available seat counts for a specific flight and cabin."
    )
    public ResponseEntity<SeatAvailabilityResponse> getAvailability(@PathVariable UUID flightId,
                                                                    @RequestParam CabinClass cabin) {
        SeatAvailabilityResponse availability = seatService.getAvailability(flightId, cabin);
        return ResponseEntity.ok(availability);
    }
}
