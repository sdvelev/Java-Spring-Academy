package com.digital.rewe.academy.medicalrecord.exception;

public class DiagnoseNotFoundException extends ObjectNotFoundException {
    public DiagnoseNotFoundException(String message, Long id) {
        super(message, id);
    }
}