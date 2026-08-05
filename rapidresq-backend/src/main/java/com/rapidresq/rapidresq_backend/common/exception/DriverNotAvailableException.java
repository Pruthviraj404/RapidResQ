package com.rapidresq.rapidresq_backend.common.exception;

import org.springframework.http.HttpStatus;

public class DriverNotAvailableException extends BaseApiException {

    public DriverNotAvailableException(String message) {
        super(HttpStatus.CONFLICT, "DRIVER_NOT_AVAILABLE", message);
    }

}

