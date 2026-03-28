package com.digital.rewe.academy.medicalrecord.exception;

public class TreatmentNotFoundException extends ObjectNotFoundException {
    public TreatmentNotFoundException(String message, Long id) {
        super(message, id);
    }
}