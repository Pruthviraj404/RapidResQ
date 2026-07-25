package com.rapidresq.rapidresq_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (

    @NotBlank(message = "Full name is required")
    @Size(max=150, message ="Full name must be at most 150 characters" )
    String fullName,


    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$" , message = "Phone number must be 7-15 digits, optionally prefixed with +")
    String phoneNumber,

    @NotBlank(message = "Password is required")
    @Size(min =8, max= 100, message ="Password must be at least 8 characters")
    String password

    
){}
