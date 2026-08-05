package com.rapidresq.rapidresq_backend.driver.repository;

import com.rapidresq.rapidresq_backend.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByUserId(UUID userId);

    Optional<Driver> findByAmbulance_Id(UUID ambulanceId);

    boolean existsByLicenseNumber(String licenseNumber);

}
