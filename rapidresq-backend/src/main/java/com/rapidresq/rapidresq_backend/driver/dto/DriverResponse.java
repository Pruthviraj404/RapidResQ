package com.rapidresq.rapidresq_backend.driver.dto;

import com.rapidresq.rapidresq_backend.ambulance.entity.Ambulance;
import com.rapidresq.rapidresq_backend.driver.entity.Driver;
import com.rapidresq.rapidresq_backend.driver.entity.DriverAvailability;

import java.util.UUID;

public record DriverResponse(

        UUID driverId,
        UUID userId,
        String fullName,
        String email,
        String phoneNumber,
        String licenseNumber,
        DriverAvailability availability,
        boolean online,
        boolean active,
        AmbulanceSummary ambulance
    )

{
    public record AmbulanceSummary(
            UUID id,
            String registrationNumber,
            String type,
            String status) {
        public static AmbulanceSummary from(Ambulance a) {
            if (a == null)
                return null;
            return new AmbulanceSummary(a.getId(), a.getRegistrationNumber(), a.getType().name(), a.getStatus().name());
        }
    }

    public static DriverResponse from(Driver driver){
         return new DriverResponse(
                driver.getId(),
                driver.getUser().getId(),
                driver.getUser().getFullName(),
                driver.getUser().getEmail(),
                driver.getUser().getPhoneNumber(),
                driver.getLicenseNumber(),
                driver.getAvailability(),
                driver.isOnline(),
                driver.isActive(),
                AmbulanceSummary.from(driver.getAmbulance())
        );

    }

}