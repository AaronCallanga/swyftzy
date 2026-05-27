package com.callanga.swyftzy.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_BASE_URI = "https://api.flightbooking.com/errors";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String ERRORS_KEY = "errors";

    // ==================== Domain Exceptions ====================

    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ProblemDetail handleSeatAlreadyBooked(SeatAlreadyBookedException ex, HttpServletRequest request) {
        log.warn("Seat already booked: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT, "Seat Already Booked",
                ex.getMessage(), "/seat-already-booked", request
                                 );
    }

    @ExceptionHandler(SeatNotFoundException.class)
    public ProblemDetail handleSeatNotFound(SeatNotFoundException ex, HttpServletRequest request) {
        log.warn("Seat not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND, "Seat Not Found",
                ex.getMessage(), "/seat-not-found", request
                                 );
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ProblemDetail handleFlightNotFound(FlightNotFoundException ex, HttpServletRequest request) {
        log.warn("Seat not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND, "Flight Not Found",
                ex.getMessage(), "/flight-not-found", request
                                 );
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ProblemDetail handleBookingNotFound(BookingNotFoundException ex, HttpServletRequest request) {
        log.warn("Booking not found: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND, "Booking Not Found",
                ex.getMessage(), "/booking-not-found", request
                                 );
    }

    @ExceptionHandler(InvalidSeatStateException.class)
    public ProblemDetail handleInvalidSeatState(InvalidSeatStateException ex, HttpServletRequest request) {
        log.warn("Invalid seat state: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT, "Invalid Seat State",
                ex.getMessage(), "/invalid-seat-state", request
                                 );
    }

    // ==================== Concurrency Exceptions ====================

    @ExceptionHandler(PessimisticLockingFailureException.class)
    public ProblemDetail handleLockTimeout(PessimisticLockingFailureException ex, HttpServletRequest request) {
        log.warn("Lock timeout during concurrent booking attempt: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT, "Concurrent Booking Conflict",
                "Unable to acquire seat lock. The seat may be in high demand. Please try again.",
                "/concurrent-booking-conflict", request
                                 );
    }

    // ==================== Validation Exceptions ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                                            .collect(Collectors.toMap(
                                                    FieldError::getField,
                                                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                                                    (a, b) -> a + "; " + b
                                                                     ));

        log.warn("Validation failed for request {}: {}", request.getRequestURI(), fieldErrors);

        ProblemDetail problem = buildProblemDetail(
                HttpStatus.BAD_REQUEST, "Validation Failed",
                "Request body validation failed. See 'errors' field for details.",
                "/validation-failed", request
                                                  );
        problem.setProperty(ERRORS_KEY, fieldErrors);
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> violations = ex.getConstraintViolations().stream()
                                           .collect(Collectors.toMap(
                                                   violation -> violation.getPropertyPath().toString(),
                                                   ConstraintViolation::getMessage,
                                                   (a, b) -> a + "; " + b
                                                                    ));

        log.warn("Constraint violation for request {}: {}", request.getRequestURI(), violations);

        ProblemDetail problem = buildProblemDetail(
                HttpStatus.BAD_REQUEST, "Constraint Violation",
                "Request parameter validation failed. See 'errors' field for details.",
                "/constraint-violation", request
                                                  );
        problem.setProperty(ERRORS_KEY, violations);
        return problem;
    }

    // ==================== Request Format Exceptions ====================

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParameter(MissingServletRequestParameterException ex,
                                                HttpServletRequest request) {
        log.warn("Missing required parameter '{}' for request {}", ex.getParameterName(), request.getRequestURI());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST, "Missing Required Parameter",
                String.format(
                        "Required parameter '%s' of type '%s' is not present",
                        ex.getParameterName(),
                        ex.getParameterType()
                             ),
                "/missing-parameter", request
                                 );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format(
                "Parameter '%s' must be of type '%s'",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
                                      );
        log.warn("Type mismatch for request {}: {}", request.getRequestURI(), message);
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST, "Parameter Type Mismatch",
                message, "/parameter-type-mismatch", request
                                 );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed request body for {}: {}", request.getRequestURI(), ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST, "Malformed Request Body",
                "The request body is malformed or cannot be read. Please check the JSON format and data types.",
                "/malformed-request", request
                                 );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST, "Invalid Argument",
                ex.getMessage(), "/invalid-argument", request
                                 );
    }

    // ==================== Fallback ====================

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error processing request {}", request.getRequestURI(), ex);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "An unexpected error occurred. Please try again later or contact support.",
                "/internal-server-error", request
                                 );
    }

    // ==================== Helpers ====================

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail,
                                             String typePath, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create(ERROR_BASE_URI + typePath));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty(TIMESTAMP_KEY, Instant.now().toString());
        return problem;
    }
}
