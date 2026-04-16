package com.example.walletsim_backend.service;

import com.example.walletsim_backend.dto.request.LoginRequest;
import com.example.walletsim_backend.dto.request.RegisterRequest;
import com.example.walletsim_backend.dto.request.ResendVerificationRequest;
import com.example.walletsim_backend.dto.request.VerifyEmailRequest;
import com.example.walletsim_backend.dto.response.ApiResponse;
import com.example.walletsim_backend.dto.response.AuthResponse;

public interface AuthService {
    ApiResponse<String> register(RegisterRequest request);
    ApiResponse<AuthResponse> verifyEmail(VerifyEmailRequest request);
    ApiResponse<String> resendVerificationOtp(ResendVerificationRequest request);
    ApiResponse<AuthResponse> login(LoginRequest request);
}
