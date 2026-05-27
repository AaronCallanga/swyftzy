package com.callanga.swyftzy.flight.service;

import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.flight.enums.FlightStatus;
import com.callanga.swyftzy.flight.repository.FlightRepository;
import com.callanga.swyftzy.shared.exception.FlightNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void getFlightById_whenFlightExists_returnsFlight() {
        UUID flightId = UUID.randomUUID();
        Flight expectedFlight = Flight.builder()
                .id(flightId)
                .flightNumber("BR123")
                .origin("TPE")
                .destination("HKG")
                .departureTime(LocalDateTime.of(2026, 6, 15, 8, 30))
                .arrivalTime(LocalDateTime.of(2026, 6, 15, 10, 45))
                .aircraftType("Boeing 777-300ER")
                .status(FlightStatus.SCHEDULED)
                .build();

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(expectedFlight));

        Flight result = flightService.getFlightById(flightId);

        assertThat(result).isEqualTo(expectedFlight);
        assertThat(result.getFlightNumber()).isEqualTo("BR123");
        assertThat(result.getOrigin()).isEqualTo("TPE");
    }

    @Test
    void getFlightById_whenFlightDoesNotExist_throwsFlightNotFoundException() {
        UUID flightId = UUID.randomUUID();

        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.getFlightById(flightId))
                .isInstanceOf(FlightNotFoundException.class)
                .hasMessageContaining(flightId.toString());
    }
}
