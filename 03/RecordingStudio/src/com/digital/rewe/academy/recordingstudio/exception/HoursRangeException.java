package com.digital.rewe.academy.recordingstudio.exception;

public class HoursRangeException extends RuntimeException {
    public HoursRangeException(String message) {
        super(message);
    }

    public HoursRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}