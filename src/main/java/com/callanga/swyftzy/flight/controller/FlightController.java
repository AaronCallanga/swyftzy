package com.callanga.swyftzy.flight.controller;

import com.callanga.swyftzy.flight.dto.FlightResponse;
import com.callanga.swyftzy.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Flights", description = "Flight search and schedule endpoints")
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    @Operation(
            summary = "Search flights",
            description = "Search for flights by origin, optional destinations, and departure date. Supports multi-value destination filtering."
    )
    public ResponseEntity<Page<FlightResponse>> searchFlights(
            @RequestParam(required = false)
            String origin,

            @RequestParam(required = false)
            List<String> destinations,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate departureDate,

            @PageableDefault(size = 10)
            Pageable pageable) {

        Page<FlightResponse> flights = flightService.searchFlights(origin, destinations, departureDate, pageable);
        return ResponseEntity.ok(flights);
    }

}
