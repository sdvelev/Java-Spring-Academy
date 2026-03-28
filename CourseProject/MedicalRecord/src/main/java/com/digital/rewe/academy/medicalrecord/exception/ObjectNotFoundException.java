package com.digital.rewe.academy.medicalrecord.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends MedicalRecordException {

    private final Long id;

    public ObjectNotFoundException(String message, Long id) {
        super(message);
        this.id=id;
    }
}