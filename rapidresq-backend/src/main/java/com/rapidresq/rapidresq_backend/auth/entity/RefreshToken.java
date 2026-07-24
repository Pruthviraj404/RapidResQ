package com.rapidresq.rapidresq_backend.auth.entity;

import com.rapidresq.rapidresq_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID )
    private UUID id;

    
    
}
