package com.digital.rewe.academy.medicalrecord.exception;

public class PersonNotFoundException extends ObjectNotFoundException {
    public PersonNotFoundException(String message, Long id) {
        super(message, id);
    }
}