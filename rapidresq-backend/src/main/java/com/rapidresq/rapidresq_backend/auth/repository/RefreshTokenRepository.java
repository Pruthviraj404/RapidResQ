package com.rapidresq.rapidresq_backend.auth.repository;


import com.rapidresq.rapidresq_backend.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked =true WHERE r.user.id =:userId AND r.revoked =false")
    void revokeAllForUser(@Param("userID") UUID userId);
    
}
