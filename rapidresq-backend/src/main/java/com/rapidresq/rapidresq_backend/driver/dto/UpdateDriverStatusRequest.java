package com.rapidresq.rapidresq_backend.driver.dto;

import com.rapidresq.rapidresq_backend.driver.entity.DriverAvailability;
import jakarta.validation.constraints.NotNull;

public record UpdateDriverStatusRequest (
    @NotNull(message = "online is required")
    Boolean online,

    DriverAvailability availability
    
)
{}