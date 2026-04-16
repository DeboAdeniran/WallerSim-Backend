package com.example.walletsim_backend.controller;

import com.example.walletsim_backend.dto.request.LoginRequest;
import com.example.walletsim_backend.dto.request.RegisterRequest;
import com.example.walletsim_backend.dto.request.ResendVerificationRequest;
import com.example.walletsim_backend.dto.request.VerifyEmailRequest;
import com.example.walletsim_backend.dto.response.ApiResponse;
import com.example.walletsim_backend.dto.response.AuthResponse;
import com.example.walletsim_backend.dto.response.UserSummary;
import com.example.walletsim_backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyEmail(@RequestBody VerifyEmailRequest request){
        ApiResponse response = authService.verifyEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<AuthResponse>> resendVerificationOtp(@RequestBody ResendVerificationRequest request){
        ApiResponse response = authService.resendVerificationOtp(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request){
        ApiResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
