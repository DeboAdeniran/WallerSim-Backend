package com.example.walletsim_backend.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
//    email, password
    private String email;
    private String password;
}
