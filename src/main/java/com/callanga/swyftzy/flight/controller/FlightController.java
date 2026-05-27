package com.callanga.swyftzy.flight.controller;

import com.callanga.swyftzy.flight.dto.FlightResponse;
import com.callanga.swyftzy.flight.repository.filter.FlightFilter;
import com.callanga.swyftzy.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
            @ParameterObject @ModelAttribute FlightFilter flightFilter,

            @PageableDefault(size = 10)
            Pageable pageable) {

        Page<FlightResponse> flights = flightService.searchFlights(flightFilter, pageable);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get flight details",
            description = "Retrieve detailed information about a specific flight including schedule and aircraft."
    )
    public ResponseEntity<FlightResponse> getFlight(@PathVariable UUID id) {

        return flightService.findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

}
