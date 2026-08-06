package com.rapidresq.rapidresq_backend.ambulance.dto;

import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceStatus;
import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceType;

public record UpdateAmbulanceRequest (
    String registrationNumber,
    AmbulanceType type,
    AmbulanceStatus status,
    Boolean active

)
    {
    
}
