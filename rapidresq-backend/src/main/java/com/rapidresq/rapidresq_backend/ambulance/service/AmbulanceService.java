package com.rapidresq.rapidresq_backend.ambulance.service;

import com.rapidresq.rapidresq_backend.ambulance.dto.AmbulanceResponse;
import com.rapidresq.rapidresq_backend.ambulance.dto.CreateAmbulanceRequest;
import com.rapidresq.rapidresq_backend.ambulance.dto.UpdateAmbulanceLocationRequest;
import com.rapidresq.rapidresq_backend.ambulance.dto.UpdateAmbulanceRequest;
import com.rapidresq.rapidresq_backend.ambulance.entity.Ambulance;
import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceStatus;
import com.rapidresq.rapidresq_backend.ambulance.repository.AmbulanceRepository;
import com.rapidresq.rapidresq_backend.common.exception.DuplicateResourceException;
import com.rapidresq.rapidresq_backend.common.exception.ForbiddenOperationException;
import com.rapidresq.rapidresq_backend.common.exception.ResourceNotFoundException;
import com.rapidresq.rapidresq_backend.driver.entity.Driver;
import com.rapidresq.rapidresq_backend.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;
    private final DriverRepository driverRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public AmbulanceResponse createAmbulance(CreateAmbulanceRequest request) {
        if (ambulanceRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new DuplicateResourceException("An ambulance with this registration number is aleady exists");

        }

        Ambulance ambulance = Ambulance.builder()
                .registrationNumber(request.registrationNumber())
                .type(request.type())
                .status(AmbulanceStatus.OFFLINE)
                .active(true)
                .build();

        ambulance = ambulanceRepository.save(ambulance);
        log.info("Ambulance registered: id={}, regNum={}", ambulance.getId(), ambulance.getRegistrationNumber());

        return AmbulanceResponse.from(ambulance);

    }

    @Transactional(readOnly = true)
    public List<AmbulanceResponse> listAmbulances() {
        return ambulanceRepository.findAll().stream().map(AmbulanceResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AmbulanceResponse getAmbulance(UUID ambulanceId) {
        return AmbulanceResponse.from(findAmbulaceOrThrow(ambulanceId));
    }

    @Transactional
    public AmbulanceResponse updateAmbulance(UUID ambulanceId, UpdateAmbulanceRequest request) {
        Ambulance ambulance = findAmbulaceOrThrow(ambulanceId);

        if (request.registrationNumber() != null
                && request.registrationNumber().equalsIgnoreCase(ambulance.getRegistrationNumber())) {
            if (ambulanceRepository.existsByRegistrationNumber(request.registrationNumber())) {
                throw new DuplicateResourceException("Registration number  aleardy in use");

            }
            ambulance.setRegistrationNumber(request.registrationNumber().toUpperCase().trim());
        }

        if (request.type() != null) {
            ambulance.setType(request.type());
        }

        if (request.status() != null) {
            ambulance.setStatus(request.status());
        }
        if (request.active() != null) {
            ambulance.setActive(request.active());
        }

        ambulance = ambulanceRepository.save(ambulance);
        log.info("Ambulance updated: id={}", ambulanceId);
        return AmbulanceResponse.from(ambulance);

    }

    @Transactional
    public AmbulanceResponse updateLocation(UUID userId, UpdateAmbulanceLocationRequest request) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No driver profile for this account"));

        Ambulance ambulance = driver.getAmbulance();

        if (ambulance == null) {
            throw new ForbiddenOperationException("Driver has no assigned ambulance to update location for");

        }

        ambulance.setCurrentLocation(
                geometryFactory.createPoint(new Coordinate(request.longitude(), request.latitude()))

        );

        ambulance.setLastLocationUpdate(Instant.now());

        ambulance = ambulanceRepository.save(ambulance);
        log.debug("Ambulance location updated: id={}, lat={}, lng={}", ambulance.getId(), request.latitude(),
                request.longitude());

        return AmbulanceResponse.from(ambulance);

    }

    private Ambulance findAmbulaceOrThrow(UUID ambulanceId) {
        return ambulanceRepository.findById(ambulanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found :" + ambulanceId));
    }

}
