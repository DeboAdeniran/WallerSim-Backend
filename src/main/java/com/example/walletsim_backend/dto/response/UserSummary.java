package com.example.walletsim_backend.dto.response;

import com.example.walletsim_backend.enums.KycTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary {
    //UserSummary — id, email, firstName, kycTier, emailVerified;
    private String id;
    private String email;
    private String firstName;
    private KycTier kycTier;
    private boolean emailVerified = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public KycTier getKycTier() {
        return kycTier;
    }

    public void setKycTier(KycTier kycTier) {
        this.kycTier = kycTier;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
