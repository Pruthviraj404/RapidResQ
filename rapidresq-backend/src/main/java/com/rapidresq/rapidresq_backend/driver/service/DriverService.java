package com.rapidresq.rapidresq_backend.driver.service;

import com.rapidresq.rapidresq_backend.ambulance.entity.Ambulance;
import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceStatus;
import com.rapidresq.rapidresq_backend.ambulance.repository.AmbulanceRepository;
import com.rapidresq.rapidresq_backend.common.exception.DuplicateResourceException;
import com.rapidresq.rapidresq_backend.common.exception.ForbiddenOperationException;
import com.rapidresq.rapidresq_backend.common.exception.ResourceNotFoundException;
import com.rapidresq.rapidresq_backend.driver.dto.AssignAmbulanceRequest;
import com.rapidresq.rapidresq_backend.driver.dto.CreateDriverRequest;
import com.rapidresq.rapidresq_backend.driver.dto.DriverResponse;
import com.rapidresq.rapidresq_backend.driver.dto.UpdateDriverStatusRequest;
import com.rapidresq.rapidresq_backend.driver.dto.DriverResponse.AmbulanceSummary;
import com.rapidresq.rapidresq_backend.driver.entity.Driver;
import com.rapidresq.rapidresq_backend.driver.entity.DriverAvailability;
import com.rapidresq.rapidresq_backend.driver.repository.DriverRepository;
import com.rapidresq.rapidresq_backend.user.entity.Role;
import com.rapidresq.rapidresq_backend.user.entity.User;
import com.rapidresq.rapidresq_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverResponse driverResponse;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final PasswordEncoder passwordEncoder;

    DriverService(DriverResponse driverResponse) {
        this.driverResponse = driverResponse;
    }

    @Transactional
    public DriverResponse createDriver(CreateDriverRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("An account with this email already exists");

        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new DuplicateResourceException("An account with this phone number already exists");

        }

        if (driverRepository.existsByLicenseNumber(request.licenceNumber())) {
            throw new DuplicateResourceException("A driver with this license number already exist");

        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.DRIVER)
                .build();

        user = userRepository.save(user);

        Driver driver = Driver.builder()
                .user(user)
                .licenseNumber(request.licenceNumber())
                .availability(DriverAvailability.OFFLINE)
                .online(false)
                .active(true)
                .build();

        driver = driverRepository.save(driver);

        log.info("Driver account created: driverId={}, userId={}", driver.getId(), user.getId());
        return DriverResponse.from(driver);
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> listDrivers() {
        return driverRepository.findAll().stream().map(DriverResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public DriverResponse getDriver(UUID driverId) {
        return driverResponse.from(findDriverOrThrow(driverId));

    }

    @Transactional
    public DriverResponse assignAmbulance(UUID driverId, AssignAmbulanceRequest request) {
        Driver driver = findDriverOrThrow(driverId);

        if (request.ambulanceId() == null) {
            driver.setAmbulance(null);
            log.info("Ambulance unassigned from driver: driverId={}", driverId);
            return DriverResponse.from(driverRepository.save(driver));

        }

        Ambulance ambulance = ambulanceRepository.findById(request.ambulanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found " + request.ambulanceId()));

        driverRepository.findByAmbulance_Id(ambulance.getId())
                .filter(existng -> !existing.getId().equals(driverId))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "This ambulance is already assigned to another driver");

                });

        driver.setAmbulance(ambulance);
        driver = driverRepository.save(driver);
        log.info("Ambulance assigned to driver: driverId={},ambulanceId={}", driverId, ambulance.getId());

    }

    @Transactional(readOnly = true)
    public DriverResponse getMyProfile(UUID userId) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No driver profile for this account"));
        return DriverResponse.from(driver);
    }

    @Transactional
    public DriverResponse updateMyStatus(UUID userId, UpdateDriverStatusRequest request) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No driver profile this account"));

        boolean goingOnline = Boolean.TRUE.equals(request.online());

        if (goingOnline && driver.getAmbulance() == null) {
            throw new ForbiddenOperationException("Cannot go online without an assign ambulance");

        }

        DriverAvailability newAvailability;
        if (!goingOnline) {
            newAvailability = DriverAvailability.OFFLINE;

        } else {
            newAvailability = request.availability() != null ? request.availability() : DriverAvailability.AVAILABLE;
            if (newAvailability == DriverAvailability.OFFLINE) {
                throw new ForbiddenOperationException("Cannot be online with OFFLINE availability");

            }

        }

        driver.setOnline(goingOnline);
        driver.setAvailability(newAvailability);
        driver = driverRepository.save(driver);

        Ambulance ambulance = driver.getAmbulance();
        if(ambulance != null){
            ambulance.setStatus(AmbulanceStatus.valueOf(newAvailability.name()));
            ambulanceRepository.save(ambulance);
        }

        log.info("Driver status updated: driverId={}, online={}, availability={}",driver.getId(),goingOnline,newAvailability);

        return DriverResponse.from(driver);


    }
    private Driver findDriverOrThrow(UUID driverId){
        return driverRepository.findById(driverId).orElseThrow(()-> new ResourceNotFoundException("Driver not found :"+driverId));
        
    }


}
