package com.virtual_power_plant.controller;

import com.virtual_power_plant.model.dto.response.GlobalApiResponse;
import com.virtual_power_plant.service.CustomMessageSource;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    /**
     * Message Source Instance
     */
    @NonNull
    protected final CustomMessageSource customMessageSource;

    protected BaseController(@NonNull CustomMessageSource customMessageSource) {
        this.customMessageSource = customMessageSource;
    }

    protected <T> ResponseEntity<GlobalApiResponse<T>> buildFetchedResponse(String message, T data) {
        return buildSuccessResponse(HttpStatus.OK, message, data);
    }

    protected <T> ResponseEntity<GlobalApiResponse<T>> buildSavedResponse(String message, T data) {
        return buildSuccessResponse(HttpStatus.CREATED, message, data);
    }

    protected <T> ResponseEntity<GlobalApiResponse<T>> buildUpdatedResponse(String message, T data) {
        return buildSuccessResponse(HttpStatus.OK, message, data);
    }

    protected <T> ResponseEntity<GlobalApiResponse<T>> buildDeletedResponse(String message) {
        return buildSuccessResponse(HttpStatus.NO_CONTENT, message, null);
    }

    private <T> ResponseEntity<GlobalApiResponse<T>> buildSuccessResponse(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(new GlobalApiResponse<>(message, data));
    }

}
