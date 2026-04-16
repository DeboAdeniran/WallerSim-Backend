package com.example.walletsim_backend.model;

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
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column(nullable = false)
    private String ipAddress;
    @Column(nullable = false)
    private String deviceInfo;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime lastSeenAt;
    private boolean revoked = false;
}
