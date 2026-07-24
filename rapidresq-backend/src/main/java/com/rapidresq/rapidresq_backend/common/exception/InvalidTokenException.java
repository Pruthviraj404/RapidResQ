package com.rapidresq.rapidresq_backend.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseApiException {
    public InvalidTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", message);
    }

}
