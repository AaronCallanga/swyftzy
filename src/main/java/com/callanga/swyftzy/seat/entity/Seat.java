package com.callanga.swyftzy.seat.entity;
import com.callanga.swyftzy.flight.entity.Flight;
import com.callanga.swyftzy.seat.enums.CabinClass;
import com.callanga.swyftzy.seat.enums.SeatLocation;
import com.callanga.swyftzy.seat.enums.SeatStatus;
import com.callanga.swyftzy.shared.exception.InvalidSeatStateException;
import com.callanga.swyftzy.shared.exception.SeatAlreadyBookedException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Represents a single seat on a specific flight.
 * Each seat is uniquely identified by its flight and seat number combination.
 * Optimistic locking via {@code @Version} is used as a safety net,
 * while pessimistic locking is applied at the repository level for booking operations.
 */
@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "seat_number", nullable = false, length = 4)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CabinClass cabin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatLocation location;

    /**
     * Optimistic locking version field.
     * Used as a safety net alongside pessimistic locking for booking operations.
     * Automatically incremented by JPA on each update.
     */
    @Version
    private Long version;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Creates a new seat for a specific flight.
     *
     * @param flight     the flight this seat belongs to
     * @param seatNumber the seat identifier (e.g., "12A")
     * @param cabin      the cabin class
     * @param location   the physical seat location
     */
    public Seat(Flight flight, String seatNumber, CabinClass cabin, SeatLocation location) {
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.cabin = cabin;
        this.location = location;
    }

    /**
     * Books this seat by transitioning its status from AVAILABLE to BOOKED.
     * This method should only be called within a transaction that holds
     * a pessimistic lock on this seat entity.
     *
     * @throws SeatAlreadyBookedException if the seat is not available
     */
    public void book() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new SeatAlreadyBookedException(seatNumber);
        }
        this.status = SeatStatus.BOOKED;
    }

    /**
     * Cancels a booking by transitioning this seat's status from BOOKED back to AVAILABLE.
     *
     * @throws InvalidSeatStateException if the seat is not currently booked
     */
    public void cancel() {
        if (this.status != SeatStatus.BOOKED) {
            throw new InvalidSeatStateException("Cannot cancel: seat " + seatNumber + " is not booked");
        }
        this.status = SeatStatus.AVAILABLE;
    }
}
