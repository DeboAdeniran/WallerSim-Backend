package com.example.walletsim_backend.dto.request;

import lombok.Data;

@Data
public class Verify2FARequest {
    // totpCode(String)
    private String  totpCode;
}
