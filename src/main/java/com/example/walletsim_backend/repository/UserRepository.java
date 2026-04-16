package com.example.walletsim_backend.repository;

import com.example.walletsim_backend.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<UserEntity> findById(UUID id);
}
