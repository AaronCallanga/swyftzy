package com.callanga.swyftzy.seat.dto;

import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatLocation;
import com.callanga.swyftzy.seat.enums.SeatStatus;

import java.util.UUID;

public record SeatResponse(
        UUID id,
        String seatNumber,
        CabinClass cabin,
        SeatStatus status,
        SeatLocation location
) {
}