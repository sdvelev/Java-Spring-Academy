package com.digital.rewe.academy.medicalrecord.exception;

public class GeneralPractitionerNotFoundException extends ObjectNotFoundException {
    public GeneralPractitionerNotFoundException(String message, Long id) {
        super(message, id);
    }
}