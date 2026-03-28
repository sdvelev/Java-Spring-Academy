package com.digital.rewe.academy.medicalrecord.exception;

public class AddressNotFoundException extends ObjectNotFoundException {
    public AddressNotFoundException(String message, Long id) {
        super(message, id);
    }
}