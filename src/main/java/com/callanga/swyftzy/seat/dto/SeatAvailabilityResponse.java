package com.callanga.swyftzy.seat.dto;

import com.callanga.swyftzy.seat.enums.CabinClass;

public record SeatAvailabilityResponse(
        CabinClass cabin,
        Long total,
        Long available
) {
}
