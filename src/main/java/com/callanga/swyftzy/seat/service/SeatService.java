package com.callanga.swyftzy.seat.service;

import com.callanga.swyftzy.seat.dto.SeatAvailabilityResponse;
import com.callanga.swyftzy.seat.dto.SeatResponse;
import com.callanga.swyftzy.seat.dto.SeatStatusCount;
import com.callanga.swyftzy.seat.entity.Seat;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import com.callanga.swyftzy.seat.mapper.SeatMapper;
import com.callanga.swyftzy.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;

    /**
     * Lists seats for a specific flight with optional cabin filter and status.
     * Supports pagination and sorting by seat number.
     *
     * @param flightId the flight UUID
     * @param cabin optional cabin class filter
     * @param status optional status filter
     * @param pageable pagination parameters
     * @return a page of seats
     */
    public Page<SeatResponse> listSeats(UUID flightId,
                                        CabinClass cabin,
                                        SeatStatus status,
                                        Pageable pageable) {
        return seatRepository
                .findSeats(flightId, cabin, status, pageable)
                .map(seatMapper::toResponse);
    }

    /**
     * Gets the availability summary for a flight (total and available seats per cabin).
     *
     * @param flightId the flight UUID
     * @param cabin    the cabin class
     * @return a SeatAvailabilityResponse with counts
     */
    public SeatAvailabilityResponse getAvailability(UUID flightId, CabinClass cabin) {
        List<SeatStatusCount> result = seatRepository.countSeatsByStatus(flightId, cabin);

        long total = result.stream()
                           .mapToLong(SeatStatusCount::getCount)
                           .sum();

        long available = result.stream()
                               .filter(r -> r.getStatus() == SeatStatus.AVAILABLE)
                               .mapToLong(SeatStatusCount::getCount)
                               .findFirst()
                               .orElse(0);

        return new SeatAvailabilityResponse(cabin, total, available);
    }

    /**
     * Finds details of a specific seat
     *
     * @param flightId the flight UUID
     * @param seatNumber the seatNumber identifier
     * @return an Optional containing the flight response if found
     */
    public Optional<SeatResponse> findById(UUID flightId,
                                           String seatNumber) {
        return seatRepository.findByFlightIdAndSeatNumber(flightId, seatNumber)
                               .map(seatMapper::toResponse);
    }
}
