package com.callanga.swyftzy.loadtest;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Load test: 100 concurrent users race to book the same seat.
 *
 * Usage:
 *   1. Start PostgreSQL:   docker compose up -d
 *   2. Start the app:      ./mvnw spring-boot:run -Dspring.profiles.active=dev
 *   3. Get a flight UUID:  psql -h localhost -U swyftzy -c "SELECT id FROM flights WHERE flight_number = 'BR789';"
 *   4. Update FLIGHT_UUID below with that UUID.
 *   5. Run simulation:     ./mvnw gatling:test
 *
 * Expected: exactly 1 booking succeeds (201), 99 fail (409).
 */
public class SeatBookingSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");

    FeederBuilder<String> passengerFeeder = csv("passengers.csv").random();

    // TODO: Replace with a real flight UUID from your database.
    // Use a flight NOT seeded in V7 (e.g. BR789, PR202, PR302, etc.). Change the SEAT_NUMBER for testing other AVAILABLE seat too
    private static final String FLIGHT_UUID = "25b22755-acf0-49fe-b697-24e042a46492";
    private static final String SEAT_NUMBER = "12B";

    ScenarioBuilder raceScenario = scenario("Concurrent Seat Booking")
            .feed(passengerFeeder)
            .exec(
                    http("Book Seat " + SEAT_NUMBER)
                            .post("/api/v1/bookings")
                            .body(StringBody("""
                                {
                                  "flightId": "%s",
                                  "seatNumber": "%s",
                                  "passengerName": "#{name}",
                                  "passengerEmail": "#{email}"
                                }
                                """.formatted(FLIGHT_UUID, SEAT_NUMBER)))
                            .check(
                                    status().in(201, 409),
                                    status().saveAs("responseStatus")
                            )
            )
            .exec(session -> {
                System.out.println(
                    "User " + session.userId() +
                    " -> Status: " + session.getString("responseStatus")
                );
                return session;
            });

    {
        setUp(
                raceScenario.injectOpen(atOnceUsers(100))
        ).protocols(httpProtocol);
    }
}
