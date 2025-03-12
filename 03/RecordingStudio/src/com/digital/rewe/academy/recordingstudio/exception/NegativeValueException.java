package com.digital.rewe.academy.recordingstudio.exception;

public class NegativeValueException extends RuntimeException {
    public NegativeValueException(String message) {
        super(message);
    }

  public NegativeValueException(String message, Throwable cause) {
    super(message, cause);
  }
}