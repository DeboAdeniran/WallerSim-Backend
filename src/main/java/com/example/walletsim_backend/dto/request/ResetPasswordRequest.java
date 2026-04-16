package com.example.walletsim_backend.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    //email(String), otp(String), newPassword(String)
    private String email;
    private String otp;
    private String newPassword;

}
