package com.example.walletsim_backend.dto.request;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    // firstName(String), lastName(String), avatarUrl(String nullable)
    private String firstName;
    private String lastName;
    @Column(nullable = true)
    private String avatarUrl;
}
