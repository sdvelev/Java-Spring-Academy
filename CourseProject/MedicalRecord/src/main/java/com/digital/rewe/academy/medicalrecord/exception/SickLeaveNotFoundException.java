package com.digital.rewe.academy.medicalrecord.exception;

public class SickLeaveNotFoundException extends ObjectNotFoundException {
    public SickLeaveNotFoundException(String message, Long id) {
        super(message, id);
    }
}