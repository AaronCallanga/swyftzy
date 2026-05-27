package com.callanga.swyftzy.seat.dto;

import com.callanga.swyftzy.seat.enums.SeatStatus;

public interface SeatStatusCount {
    SeatStatus getStatus();
    Long getCount();
}
