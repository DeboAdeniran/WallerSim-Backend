package com.example.walletsim_backend.model;

import com.example.walletsim_backend.enums.CoinType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;
    @Column(unique = true)
    private String address;
    @Enumerated(EnumType.STRING)
    private CoinType coin;
    private BigDecimal balance = BigDecimal.ZERO;
    private String label;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
