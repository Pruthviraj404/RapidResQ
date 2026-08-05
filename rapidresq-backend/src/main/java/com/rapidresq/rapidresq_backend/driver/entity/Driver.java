package com.rapidresq.rapidresq_backend.driver.entity;

import com.rapidresq.rapidresq_backend.ambulance.entity.Ambulance;
import com.rapidresq.rapidresq_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DriverAvailability availability = DriverAvailability.OFFLINE;

    @Column(name = "is_online", nullable = false)
    @Builder.Default
    private boolean online = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_id", unique = true)
    private Ambulance ambulance;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public boolean isDispatchEligible() {
        return active && online && availability == DriverAvailability.AVAILABLE && ambulance != null;

    }

}
