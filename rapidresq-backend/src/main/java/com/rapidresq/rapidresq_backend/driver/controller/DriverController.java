package com.rapidresq.rapidresq_backend.driver.controller;

import com.rapidresq.rapidresq_backend.common.ApiResponse;
import com.rapidresq.rapidresq_backend.driver.dto.AssignAmbulanceRequest;
import com.rapidresq.rapidresq_backend.driver.dto.CreateDriverRequest;
import com.rapidresq.rapidresq_backend.driver.dto.DriverResponse;
import com.rapidresq.rapidresq_backend.driver.dto.UpdateDriverStatusRequest;
import com.rapidresq.rapidresq_backend.driver.service.DriverService;
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
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DriverResponse>> createDriver(@Valid @RequestBody CreateDriverRequest request) {
        DriverResponse response = driverService.createDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, "Driver account created"));

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DISPATCHER')")
    public ResponseEntity<ApiResponse<List<DriverResponse>>> listDrivers() {
        return ResponseEntity.ok(ApiResponse.success(driverService.listDrivers()));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DISPATCHER')")
    public ResponseEntity<ApiResponse<DriverResponse>> getDriver(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(driverService.getDriver(id)));

    }

    @PostMapping("/{id}/ambulance")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<DriverResponse>> assignAmbulance(@PathVariable UUID id,
            @Valid @RequestBody AssignAmbulanceRequest request) {
        DriverResponse response = driverService.assignAmbulance(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Ambulance assignment updated"));

    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<DriverResponse>> getMyProfile(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.success(driverService.getMyProfile(currentUser.getId())));

    }

    @PutMapping("/status")
    public ResponseEntity<ApiResponse<DriverResponse>> updateMyStatus(@AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdateDriverStatusRequest request) {
        DriverResponse response = driverService.updateMyStatus(currentUser.getId(), request);

        return ResponseEntity.ok(ApiResponse.success(response, "Status updated"));

    }

}
