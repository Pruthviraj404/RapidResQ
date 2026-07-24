package com.rapidresq.rapidresq_backend.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseApiException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "message");
    }

}
