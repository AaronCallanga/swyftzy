package com.callanga.swyftzy.flight.repository.filter;

import com.callanga.swyftzy.flight.enums.FlightStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class FlightFilter {
    private String origin;
    private List<String> destinations;
    private LocalDate departureDate;
    private FlightStatus status;
}
