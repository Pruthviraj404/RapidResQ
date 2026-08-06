package com.rapidresq.rapidresq_backend.ambulance.dto;

import com.rapidresq.rapidresq_backend.ambulance.entity.Ambulance;
import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceStatus;
import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceType;

import java.time.Instant;
import java.util.UUID;

public record AmbulanceResponse(
        UUID id,
        String registrationNumber,
        AmbulanceType type,
        AmbulanceStatus status,
        Double latitude,
        Double longitude,
        Instant lastLocationUpdate,
        boolean active)
         {
    public static AmbulanceResponse from(Ambulance ambulance) {
        Double lat = null;
        Double lng = null;

        if (ambulance.getCurrentLocation() != null) {
            lat = ambulance.getCurrentLocation().getX();
            lng = ambulance.getCurrentLocation().getY();
        }

        return new AmbulanceResponse(
                ambulance.getId(), ambulance.getRegistrationNumber(), ambulance.getType(), ambulance.getStatus(), lat,
                lng, ambulance.getLastLocationUpdate(), ambulance.isActive()
            );

    }
}