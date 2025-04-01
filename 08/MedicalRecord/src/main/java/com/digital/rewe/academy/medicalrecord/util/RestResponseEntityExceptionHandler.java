package com.digital.rewe.academy.medicalrecord.util;

import com.digital.rewe.academy.medicalrecord.exception.InvalidDateRangeException;
import com.digital.rewe.academy.medicalrecord.exception.ObjectNotFoundException;
import com.digital.rewe.academy.medicalrecord.exception.UnpaidHealthInsuranceException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//        @ExceptionHandler({AuthenticationException.class})
//    public ResponseEntity<Object> handleAuthenticationException(
//            Exception ex, WebRequest request) {
//        return new ResponseEntity<Object>(
//                "Authentication Failed!", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
//    }
//    @ExceptionHandler({AccessDeniedException.class})
//    public ResponseEntity<Object> handleAccessDeniedException(
//            Exception exception, WebRequest request) {
//        return new ResponseEntity<Object>(
//                "Access denied for this user!", new HttpHeaders(), HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(ObjectNotFoundException exception, WebRequest request) {
        LinkedHashMap<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("Description:", request.getDescription(false));
        responseBody.put("Status Code:", String.valueOf(HttpStatus.NOT_FOUND));
        responseBody.put("Message:", exception.getMessage() + ": " + exception.getId());

        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({UnpaidHealthInsuranceException.class})
    public ResponseEntity<Object> handleUnpaidHealthInsuranceException(UnpaidHealthInsuranceException exception, WebRequest request) {
        LinkedHashMap<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("Description:", request.getDescription(false));
        responseBody.put("Status Code:", String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY));
        responseBody.put("Message:", exception.getMessage() + ": " + exception.getId());

        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<Object> handleInvalidDateRangeException(InvalidDateRangeException exception, WebRequest request) {
        LinkedHashMap<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("Description:", request.getDescription(false));
        responseBody.put("Status Code:", String.valueOf(HttpStatus.BAD_REQUEST));
        responseBody.put("Message:", exception.getMessage());

        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException exception, WebRequest request) {
        LinkedHashMap<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("Type:", "ValidationException");
        responseBody.put("Description:", request.getDescription(false));
        responseBody.put("Status Code:", String.valueOf(HttpStatus.BAD_REQUEST));
        responseBody.put("Message:", exception.getMessage());

        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        LinkedHashMap<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("Type:", "ConstraintViolationException");
        responseBody.put("Description:", request.getDescription(false));
        responseBody.put("Status Code:", String.valueOf(HttpStatus.BAD_REQUEST));
        responseBody.put("Message:", exception.getMessage());

        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}