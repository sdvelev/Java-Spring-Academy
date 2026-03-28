package com.digital.rewe.academy.medicalrecord.exception;

public class SpecialtyNotFoundException extends ObjectNotFoundException {
    public SpecialtyNotFoundException(String message, Long id) {
        super(message, id);
    }
}