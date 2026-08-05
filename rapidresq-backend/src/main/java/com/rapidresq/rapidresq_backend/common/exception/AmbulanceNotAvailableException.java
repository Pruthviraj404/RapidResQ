package com.rapidresq.rapidresq_backend.common.exception;
import org.springframework.http.HttpStatus;

public class AmbulanceNotAvailableException extends BaseApiException {
    public AmbulanceNotAvailableException(String message){
        super(HttpStatus.CONFLICT, "AMBULANCE_NOT_AVAILABLE", message);
    }
    
}
