package com.digital.rewe.academy.shop.exception;

public class NoSuchGoodException extends RuntimeException {
    public NoSuchGoodException(String message) {
        super(message);
    }

    public NoSuchGoodException(String message, Throwable cause) {
        super(message, cause);
    }
}