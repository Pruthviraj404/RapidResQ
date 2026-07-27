package com.rapidresq.rapidresq_backend.auth.controller;

import com.rapidresq.rapidresq_backend.auth.service.AuthService;
import com.rapidresq.rapidresq_backend.auth.dto.AuthResponse;
import com.rapidresq.rapidresq_backend.auth.dto.LoginRequest;
import com.rapidresq.rapidresq_backend.auth.dto.RefreshTokenRequest;
import com.rapidresq.rapidresq_backend.auth.dto.RegisterRequest;
import com.rapidresq.rapidresq_backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response,"Account created successfully"));

    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response,"Login successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request){
        AuthResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success(response,"Token refreshed"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>>logout(@Valid @RequestBody RefreshTokenRequest request){
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.success(null,"Logged out"));
    }
    
}
