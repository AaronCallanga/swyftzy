package com.callanga.swyftzy.flight.repository.filter;

import com.callanga.swyftzy.flight.enums.FlightStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class FlightFilter {
    private String origin;
    private List<String> destinations;
    private LocalDateTime startOfDay;
    private LocalDateTime endOfDay;
    private FlightStatus status;
}
