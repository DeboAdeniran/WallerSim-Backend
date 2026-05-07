package com.example.walletsim_backend.repository;

import com.example.walletsim_backend.enums.OtpType;
import com.example.walletsim_backend.model.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByEmail(String email);
    List<OtpEntity> findAllByEmail(String email);
    Optional<OtpEntity> findByCode(String code);
    Boolean existsByEmail(String email);
    Optional<OtpEntity> findByEmailAndTypeAndUsedFalse(String email, OtpType type);
}
