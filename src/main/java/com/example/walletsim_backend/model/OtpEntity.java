package com.example.walletsim_backend.model;

import com.example.walletsim_backend.enums.OtpType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;
    private String email;
    private String code;
    @Enumerated(EnumType.STRING)
    private OtpType type;
    private LocalDateTime expiresAt;
    private boolean used = false;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
