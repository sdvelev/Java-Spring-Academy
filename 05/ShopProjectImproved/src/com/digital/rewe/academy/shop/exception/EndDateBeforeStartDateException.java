package com.digital.rewe.academy.shop.exception;

public class EndDateBeforeStartDateException extends RuntimeException {
    public EndDateBeforeStartDateException(String message) {
        super(message);
    }

    public EndDateBeforeStartDateException(String message, Throwable cause) {
        super(message, cause);
    }
}