package com.digital.rewe.academy.shop.exception;

public class NoCustomerWaitingException extends RuntimeException {
    public NoCustomerWaitingException(String message) {
        super(message);
    }

    public NoCustomerWaitingException(String message, Throwable cause) {
        super(message, cause);
    }
}