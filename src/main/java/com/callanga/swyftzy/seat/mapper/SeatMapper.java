package com.callanga.swyftzy.seat.mapper;

import com.callanga.swyftzy.seat.dto.SeatResponse;
import com.callanga.swyftzy.seat.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {
    public SeatResponse toResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getCabin(),
                seat.getStatus(),
                seat.getLocation()
        );
    }
}
