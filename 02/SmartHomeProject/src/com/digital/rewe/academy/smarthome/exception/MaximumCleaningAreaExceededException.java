package com.digital.rewe.academy.smarthome.exception;

public class MaximumCleaningAreaExceededException extends Exception {
    public MaximumCleaningAreaExceededException(String message) {
        super(message);
    }

    public MaximumCleaningAreaExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}