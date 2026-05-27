package com.callanga.swyftzy.booking.util;

import com.callanga.swyftzy.flight.entity.Flight;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;

// Generates unique booking references for confirmed seat reservations.
@Component
public class BookingReferenceGenerator {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SUFFIX_LENGTH = 4;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final SecureRandom random;

    public BookingReferenceGenerator() {
        this.random = new SecureRandom();
    }

    /**
     * Generates a unique booking reference for the given flight and seat.
     *
     * @param flight     the flight being booked
     * @param seatNumber the seat number being reserved (e.g., "12A")
     * @return a unique booking reference string
     */
    public String generate(Flight flight, String seatNumber) {
        String flightNumber = flight.getFlightNumber();
        String departureDate = flight.getDepartureTime().format(DATE_FORMATTER);
        String suffix = generateRandomSuffix();

        return String.format("%s-%s-%s-%s", flightNumber, seatNumber, departureDate, suffix);
    }

    /**
     * Generates a random alphanumeric suffix of the configured length.
     *
     * @return a random alphanumeric string
     */
    private String generateRandomSuffix() {
        StringBuilder sb = new StringBuilder(SUFFIX_LENGTH);
        for (int i = 0; i < SUFFIX_LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }
        return sb.toString();
    }
}

