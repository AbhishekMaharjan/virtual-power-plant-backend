package com.virtual_power_plant.exception;

import com.virtual_power_plant.model.dto.response.GlobalApiResponse;
import com.virtual_power_plant.service.CustomMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String MESSAGE = "Error";

    public CustomControllerAdvice(CustomMessageSource customMessageSource) {
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        GlobalApiResponse<Void> apiError = new GlobalApiResponse<>(false, MESSAGE, null,
                Collections.singletonList(message));
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

}