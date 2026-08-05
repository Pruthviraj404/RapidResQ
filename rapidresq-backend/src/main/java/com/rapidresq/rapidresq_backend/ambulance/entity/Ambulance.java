package com.rapidresq.rapidresq_backend.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ambulances")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name= "registration_number", nullable = false, unique  = true, length = 50)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AmbulanceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    @Builder.Default
    private AmbulanceStatus status = AmbulanceStatus.OFFLINE;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point currentLocation;

    @Column(name="last_location_update")
    private Instant lastLocationUpdate;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false , updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;
}
