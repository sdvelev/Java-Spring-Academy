package com.digital.rewe.academy.medicalrecord.exception;

public class PatientNotFoundException extends ObjectNotFoundException {
    public PatientNotFoundException(String message, Long id) {
        super(message, id);
    }
}