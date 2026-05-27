package com.callanga.swyftzy.booking.repository;
import com.callanga.swyftzy.booking.entity.Booking;
import com.callanga.swyftzy.booking.enums.BookingStatus;
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

    boolean existsByFlightIdAndSeatIdAndStatus(UUID flightId, UUID seatId, BookingStatus status);

    List<Booking> findByFlightIdAndStatus(UUID flightId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.flight.id = :flightId AND b.seat.id = :seatId")
    Optional<Booking> findByFlightIdAndSeatId(
            @Param("flightId") UUID flightId,
            @Param("seatId") UUID seatId);

    @Query("SELECT b FROM Booking b WHERE b.flight.id = :flightId AND b.seat.id = :seatId AND b.status = 'CONFIRMED'")
    Optional<Booking> findActiveBookingByFlightIdAndSeatId(
            @Param("flightId") UUID flightId,
            @Param("seatId") UUID seatId);
}