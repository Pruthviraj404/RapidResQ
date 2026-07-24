package com.rapidresq.rapidresq_backend.common;

import com.rapidresq.rapidresq_backend.common.exception.BaseApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalExceptionHandler {

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(BaseApiException ex, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(ex.getStatus().value(), ex.getErrorCode(), ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));

        ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "VALIDATION_FAILED",
                "One or more fields are invalidull", request.getRequestURI(), fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }


    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse>handleBadCredentials(Exception ex,HttpServletRequest request){
        ErrorResponse body = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "INVALID_CREDENTIALS", "Invalid email or password", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e,HttpServletRequest request){
        ErrorResponse body = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "ACCESS_DENIED", "You do not have permission to perform this action", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>handleUnexpected(Exception ex, HttpServletRequest request){
        ErrorResponse body = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_ERROR", "An unexpected error occurred", request.getRequestURI());

        return ResponseEntity.internalServerError().body(body);
    }








}
