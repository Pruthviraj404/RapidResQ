package com.rapidresq.rapidresq_backend.ambulance.dto;

import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAmbulanceRequest(
    @NotBlank(message = "Registration number is required")
    @Size(max = 50, message = "Registration number must be at most 50 characters")
    String registrationNumber,

    @NotNull(message = "Ambulance type is required")
    AmbulanceType type
){}
