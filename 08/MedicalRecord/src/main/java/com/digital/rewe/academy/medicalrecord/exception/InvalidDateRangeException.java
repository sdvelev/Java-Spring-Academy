package com.digital.rewe.academy.medicalrecord.exception;

public class InvalidDateRangeException extends MedicalRecordException {
    public InvalidDateRangeException(String message) {
        super(message);
    }

    public InvalidDateRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}