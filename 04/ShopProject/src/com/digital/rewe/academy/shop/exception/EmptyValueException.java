package com.digital.rewe.academy.shop.exception;

public class EmptyValueException extends RuntimeException {
    public EmptyValueException(String message) {
        super(message);
    }

  public EmptyValueException(String message, Throwable cause) {
    super(message, cause);
  }
}