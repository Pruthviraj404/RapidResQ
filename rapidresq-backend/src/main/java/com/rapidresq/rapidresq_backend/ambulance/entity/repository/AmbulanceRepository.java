package com.rapidresq.rapidresq_backend.ambulance.entity.repository;

import com.rapidresq.rapidresq_backend.ambulance.entity.Ambulance;
import com.rapidresq.rapidresq_backend.ambulance.entity.AmbulanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AmbulanceRepository extends JpaRepository<Ambulance, UUID> {

    Optional<Ambulance> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<Ambulance> findByActiveTrue();

    @Query(value = """
            SELECT a.* FROM ambulances a
            WHERE a.status ='AVAILABLE'
            AND a.active = true
            AND ST_Dwithin(
            a.current_location,
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
            :radiusMeters
            )

            ORDERY BY ST_Distance(
            a.current_location,
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography

            ) ASC
            """, nativeQuery = true)
    List<Ambulance> findNearbyAvailable(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radiusMeters") double radiusMeters,
            @Param("type") AmbulanceType type);

}
