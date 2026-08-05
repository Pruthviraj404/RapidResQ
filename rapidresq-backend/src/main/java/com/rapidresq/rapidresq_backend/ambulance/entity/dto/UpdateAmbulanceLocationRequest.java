package com.rapidresq.rapidresq_backend.ambulance.entity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateAmbulanceLocationRequest(
        @NotNull(message = "Latitude is required") @Min(value = -90, message = "Latitude must be >=90") @Max(value = 90, message = "Latitude must be <= 90") Double latitude,

        @NotNull(message = "Longitude is required") @Min(value = -180, message = "Longitude must be >= -180") @Max(value = 180, message = "Longitude must be <= 180") Double longitude

) {

}
