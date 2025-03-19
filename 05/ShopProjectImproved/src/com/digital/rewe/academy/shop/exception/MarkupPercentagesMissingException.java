package com.digital.rewe.academy.shop.exception;

public class MarkupPercentagesMissingException extends RuntimeException {
    public MarkupPercentagesMissingException(String message) {
        super(message);
    }

    public MarkupPercentagesMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}