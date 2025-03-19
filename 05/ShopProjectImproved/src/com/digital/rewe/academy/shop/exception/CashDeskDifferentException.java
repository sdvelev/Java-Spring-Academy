package com.digital.rewe.academy.shop.exception;

public class CashDeskDifferentException extends RuntimeException {
    public CashDeskDifferentException(String message) {
        super(message);
    }

    public CashDeskDifferentException(String message, Throwable cause) {
        super(message, cause);
    }
}