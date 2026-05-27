package com.callanga.swyftzy.flight.controller;

import com.callanga.swyftzy.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

}
