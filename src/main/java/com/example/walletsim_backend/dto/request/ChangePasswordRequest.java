package com.example.walletsim_backend.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    //currentPassword(String), newPassword(String)
    private String currentPassword;
    private String newPassword;
}
