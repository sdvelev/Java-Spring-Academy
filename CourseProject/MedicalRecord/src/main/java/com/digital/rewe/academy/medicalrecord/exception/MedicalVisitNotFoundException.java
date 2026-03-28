package com.digital.rewe.academy.medicalrecord.exception;

public class MedicalVisitNotFoundException extends ObjectNotFoundException {
    public MedicalVisitNotFoundException(String message, Long id) {
        super(message, id);
    }
}