package com.rapidresq.rapidresq_backend.common.exception;
import org.springframework.http.HttpStatus;


public class InvalidCredentialsException extends BaseApiException {
    public InvalidCredentialsException(String message){
        super(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", message);
    }
    
}
