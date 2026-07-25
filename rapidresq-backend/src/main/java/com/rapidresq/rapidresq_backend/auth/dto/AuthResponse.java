package com.rapidresq.rapidresq_backend.auth.dto;

import java.util.UUID;

public record AuthResponse (
    UUID userId,
    String fullName,
    String email,
    String role,
    String accessToken,
    String refreshToken,
    long expiresInMs



    
){}
