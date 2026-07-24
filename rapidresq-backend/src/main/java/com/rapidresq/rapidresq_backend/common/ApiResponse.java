package com.rapidresq.rapidresq_backend.common;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    T data,
    String message,
    Instant timestamp
){
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<T>(true, data, null, Instant.now());


    }

    public static <T> ApiResponse<T> success(T data,String messsage){
        return new ApiResponse<T>(true, data, messsage, Instant.now());

    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<T>(false, null, message, Instant.now());
    }
}
