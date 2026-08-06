package com.rapidresq.rapidresq_backend.ambulance.controller;

import com.rapidresq.rapidresq_backend.ambulance.dto.AmbulanceResponse;
import com.rapidresq.rapidresq_backend.ambulance.dto.CreateAmbulanceRequest;
import com.rapidresq.rapidresq_backend.ambulance.dto.UpdateAmbulanceLocationRequest;
import com.rapidresq.rapidresq_backend.ambulance.dto.UpdateAmbulanceRequest;
import com.rapidresq.rapidresq_backend.ambulance.service.AmbulanceService;
import com.rapidresq.rapidresq_backend.common.ApiResponse;
import com.rapidresq.rapidresq_backend.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ambulances")
@RequiredArgsConstructor
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AmbulanceResponse>> createAmbulance(
            @Valid @RequestBody CreateAmbulanceRequest request) {
        AmbulanceResponse response = ambulanceService.createAmbulance(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Ambulance registered successfully"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DISPATCHER')")
    public ResponseEntity<ApiResponse<List<AmbulanceResponse>>> listAmbulances() {
        return ResponseEntity.ok(ApiResponse.success(ambulanceService.listAmbulances()));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DISPATCHER')")
    public ResponseEntity<ApiResponse<AmbulanceResponse>> getAmbulance(@PathVariable UUID id) {

        return ResponseEntity.ok(ApiResponse.success(ambulanceService.getAmbulance(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AmbulanceResponse>> updateAmbulance(@PathVariable UUID id,
            @Valid @RequestBody UpdateAmbulanceRequest request) {
        AmbulanceResponse response = ambulanceService.updateAmbulance(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Ambulance updated successfully"));
    }

    @PutMapping("/location")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AmbulanceResponse>> updateLocation(@AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdateAmbulanceLocationRequest request) {
        AmbulanceResponse response = ambulanceService.updateLocation(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Location updated succefully"));
    }

}
