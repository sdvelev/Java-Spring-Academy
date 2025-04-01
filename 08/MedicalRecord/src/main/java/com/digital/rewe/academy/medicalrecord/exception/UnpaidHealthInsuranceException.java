package com.digital.rewe.academy.medicalrecord.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnpaidHealthInsuranceException extends MedicalRecordException {
  private final Long id;

  public UnpaidHealthInsuranceException(String message, Long id) {
    super(message);
    this.id=id;
  }
}