package com.digital.rewe.academy.shop.exception;

public class CashDeskClosedException extends RuntimeException {
    public CashDeskClosedException(String message) {
        super(message);
    }

    public CashDeskClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}