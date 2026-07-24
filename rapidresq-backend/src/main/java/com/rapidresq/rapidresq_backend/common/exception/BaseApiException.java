package com.rapidresq.rapidresq_backend.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseApiException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected BaseApiException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

}

