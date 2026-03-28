package com.digital.rewe.academy.medicalrecord.exception;

public class DoctorNotFoundException extends ObjectNotFoundException {
    public DoctorNotFoundException(String message, Long id) {
        super(message, id);
    }
}