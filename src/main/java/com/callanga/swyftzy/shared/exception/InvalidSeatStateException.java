package com.callanga.swyftzy.shared.exception;

public class InvalidSeatStateException extends RuntimeException {

    /**
     * Creates a new exception with a descriptive message about the invalid state.
     *
     * @param message detailed description of why the state is invalid
     */
    public InvalidSeatStateException(String message) {
        super(message);
    }
}