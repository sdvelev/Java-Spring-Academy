package com.digital.rewe.academy.shop.exception;

public class NoSuchCashDeskException extends RuntimeException {
  public NoSuchCashDeskException(String message) {
    super(message);
  }

  public NoSuchCashDeskException(String message, Throwable cause) {
    super(message, cause);
  }
}
