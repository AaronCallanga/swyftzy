package com.callanga.swyftzy.seat.repository;

import com.callanga.swyftzy.seat.dto.SeatStatusCount;
import com.callanga.swyftzy.seat.entity.Seat;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s WHERE s.flight.id = :flightId AND s.seatNumber = :seatNumber")
    Optional<Seat> findByFlightIdAndSeatNumber(
            @Param("flightId") UUID flightId,
            @Param("seatNumber") String seatNumber);

    @Query("""
                SELECT s
                FROM Seat s
                WHERE s.flight.id = :flightId
                  AND (:cabin IS NULL OR s.cabin = :cabin)
                  AND (:status IS NULL OR s.status = :status)
                ORDER BY s.seatNumber
            """)
    Page<Seat> findSeats(
            @Param("flightId") UUID flightId,
            @Param("cabin") CabinClass cabin,
            @Param("status") SeatStatus status,
            Pageable pageable
                        );

    @Query("""
                SELECT s.status AS status, COUNT(s) AS count
                FROM Seat s
                WHERE s.flight.id = :flightId
                  AND s.cabin = :cabin
                GROUP BY s.status
            """)
    List<SeatStatusCount> countSeatsByStatus(
            @Param("flightId") UUID flightId,
            @Param("cabin") CabinClass cabin
                                            );
}
