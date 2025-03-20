package com.digital.rewe.academy.printinghouse.exception;

public class WrongDateIntervalException extends RuntimeException {
    public WrongDateIntervalException(String message) {
        super(message);
    }

    public WrongDateIntervalException(String message, Throwable cause) {
        super(message, cause);
    }
}