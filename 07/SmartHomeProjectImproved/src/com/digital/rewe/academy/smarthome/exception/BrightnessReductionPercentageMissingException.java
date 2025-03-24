package com.digital.rewe.academy.smarthome.exception;

public class BrightnessReductionPercentageMissingException extends RuntimeException {
    public BrightnessReductionPercentageMissingException(String message) {
        super(message);
    }

    public BrightnessReductionPercentageMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}