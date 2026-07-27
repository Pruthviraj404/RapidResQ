package com.rapidresq.rapidresq_backend.auth.service;

import com.rapidresq.rapidresq_backend.auth.entity.RefreshToken;
import com.rapidresq.rapidresq_backend.auth.repository.RefreshTokenRepository;
import com.rapidresq.rapidresq_backend.auth.dto.AuthResponse;
import com.rapidresq.rapidresq_backend.auth.dto.LoginRequest;
import com.rapidresq.rapidresq_backend.auth.dto.RefreshTokenRequest;
import com.rapidresq.rapidresq_backend.auth.dto.RegisterRequest;
import com.rapidresq.rapidresq_backend.common.exception.DuplicateResourceException;
import com.rapidresq.rapidresq_backend.common.exception.InvalidCredentialsException;
import com.rapidresq.rapidresq_backend.common.exception.InvalidTokenException;
import com.rapidresq.rapidresq_backend.security.JwtService;
import com.rapidresq.rapidresq_backend.user.entity.Role;
import com.rapidresq.rapidresq_backend.user.entity.User;
import com.rapidresq.rapidresq_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("An account with this email already exists");

        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new DuplicateResourceException("An account with this phone number already exists");

        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email().toLowerCase())
                .phoneNumber(request.phoneNumber())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        log.info("New user registered: userId={}", user.getId());

        return issueTokens(user);

    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password()));

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        log.info("User Logged in: userId={}", user.getId());

        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        String hash = hash(request.refreshToken());

        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new InvalidTokenException("Refresh token is invalid"));

        if (!stored.isvalid()) {
            throw new InvalidTokenException("Refresh token has expired or been revoked");

        }

        stored.setRevoked(true);

        refreshTokenRepository.save(stored);

        return issueTokens(stored.getUser());
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        String hash = hash(request.refreshToken());
        refreshTokenRepository.findByTokenHash(hash).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String rawRefreshToken = jwtService.generateRawRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(hash(rawRefreshToken))
                .expiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()))
                .build();

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                accessToken,
                rawRefreshToken,
                jwtService.getRefreshTokenExpirationMs());

    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(rawToken.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }

}
