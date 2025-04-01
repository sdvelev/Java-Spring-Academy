package com.digital.rewe.academy.medicalrecord.exception;

public class MedicalRecordException extends RuntimeException {
    public MedicalRecordException(String message) {
        super(message);
    }

    public MedicalRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}