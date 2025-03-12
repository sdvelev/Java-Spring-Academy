package com.digital.rewe.academy.smarthome.exception;

public class NegativeValueException extends Exception {
    public NegativeValueException(String message) {
        super(message);
    }

    public NegativeValueException(String message, Throwable cause) {
        super(message, cause);
    }
}