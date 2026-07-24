package com.rapidresq.rapidresq_backend.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseApiException {
    public DuplicateResourceException(String message) {
        super(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE", message);
    }

}
