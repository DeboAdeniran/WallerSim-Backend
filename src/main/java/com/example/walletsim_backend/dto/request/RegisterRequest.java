package com.example.walletsim_backend.dto.request;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
@Data
public class RegisterRequest {
// firstName(String), lastName(String), email(String), password(String), referralCode(String nullable)
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Column(nullable = true)
    private String referralCode;
}
