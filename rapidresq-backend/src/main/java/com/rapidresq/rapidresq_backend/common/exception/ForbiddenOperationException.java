package com.rapidresq.rapidresq_backend.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenOperationException extends BaseApiException {
    public ForbiddenOperationException(String message) {
        super(HttpStatus.FORBIDDEN, "FORBIDDEN_OPERATION", message);
    }

}
