package com.callanga.swyftzy.booking.repository;
import com.callanga.swyftzy.booking.entity.Booking;
import com.callanga.swyftzy.booking.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Optional<Booking> findByBookingReference(String bookingReference);

    Page<Booking> findByFlightId(UUID flightId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.flight.id = :flightId AND b.seat.id = :seatId")
    Optional<Booking> findByFlightIdAndSeatId(
            @Param("flightId") UUID flightId,
            @Param("seatId") UUID seatId);

}