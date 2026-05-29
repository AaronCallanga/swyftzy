package com.callanga.swyftzy.booking.controller;

import com.callanga.swyftzy.booking.dto.BookingRequest;
import com.callanga.swyftzy.booking.service.BookingService;
import com.callanga.swyftzy.shared.exception.BookingNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new com.callanga.swyftzy.shared.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void createBooking_withInvalidEmail_returns400() throws Exception {
        BookingRequest request = new BookingRequest(
                UUID.randomUUID(),
                "12A",
                "John Doe",
                "not-an-email"
        );

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Failed"));
    }

    @Test
    void cancelBooking_withNonExistentReference_returns404() throws Exception {
        String reference = "NONEXISTENT";
        when(bookingService.cancelBooking(reference))
                .thenThrow(new BookingNotFoundException(reference));

        mockMvc.perform(delete("/api/v1/bookings/{reference}", reference))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Booking Not Found"));
    }

    @Test
    void createBooking_withBlankSeatNumber_returns400() throws Exception {
        BookingRequest request = new BookingRequest(
                UUID.randomUUID(),
                "",
                "John Doe",
                "john@example.com"
        );

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.seatNumber").exists());
    }

    @Test
    void getBookingByReference_withNonExistentReference_returns404() throws Exception {
        String reference = "NOBOOKING";
        when(bookingService.getBookingByReference(reference)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/v1/bookings/reference/{reference}", reference))
                .andExpect(status().isNotFound());
    }
}
