package com.digital.rewe.academy.shop.exception;

public class NoSuchQuantityException extends RuntimeException {
    public NoSuchQuantityException(String message) {
        super(message);
    }

  public NoSuchQuantityException(String message, Throwable cause) {
    super(message, cause);
  }
}