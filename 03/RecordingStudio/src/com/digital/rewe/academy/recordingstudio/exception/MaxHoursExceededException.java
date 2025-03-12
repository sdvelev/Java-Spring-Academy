package com.digital.rewe.academy.recordingstudio.exception;

public class MaxHoursExceededException extends Exception {
    public MaxHoursExceededException(String message) {
        super(message);
    }

    public MaxHoursExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
