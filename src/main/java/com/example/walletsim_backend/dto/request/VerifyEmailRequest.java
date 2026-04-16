package com.example.walletsim_backend.dto.request;

import lombok.Data;

@Data
public class VerifyEmailRequest {
//    email(String), otp(String)
    private String email;
    private String otp;
}
