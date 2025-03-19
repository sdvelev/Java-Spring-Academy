package com.digital.rewe.academy.shop.exception;

public class NoSuchCashierException extends RuntimeException {
    public NoSuchCashierException(String message) {
        super(message);
    }

    public NoSuchCashierException(String message, Throwable cause) {
        super(message, cause);
    }
}