package com.rapidresq.rapidresq_backend.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.rapidresq.rapidresq_backend.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {
    
    Optional<User>findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
