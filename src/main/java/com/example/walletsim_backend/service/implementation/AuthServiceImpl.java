package com.example.walletsim_backend.service.implementation;

import com.example.walletsim_backend.dto.request.*;
import com.example.walletsim_backend.dto.response.ApiResponse;
import com.example.walletsim_backend.dto.response.AuthResponse;
import com.example.walletsim_backend.dto.response.ErrorDetail;
import com.example.walletsim_backend.dto.response.UserSummary;
import com.example.walletsim_backend.enums.ErrorCode;
import com.example.walletsim_backend.enums.OtpType;
import com.example.walletsim_backend.model.OtpEntity;
import com.example.walletsim_backend.model.UserEntity;
import com.example.walletsim_backend.repository.OtpRepository;
import com.example.walletsim_backend.repository.UserRepository;
import com.example.walletsim_backend.service.AuthService;
import com.example.walletsim_backend.service.EmailService;
import com.example.walletsim_backend.util.OTPGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository, OtpRepository otpRepository, AuthenticationManager authenticationManager, JwtService jwtService, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    @Override
    public ApiResponse<String> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            ErrorDetail error = new ErrorDetail(ErrorCode.EMAIL_TAKEN.name(), null);
            return ApiResponse.error("Email is already in use",error, 409);
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
//                .referralCode(request.getReferralCode())
                .build();
        UserEntity createdUser = userRepository.save(user);

        OtpEntity otpEntity = OtpEntity.builder()
                .email(request.getEmail())
                .code(OTPGenerator.generateOTP())
                .type(OtpType.EMAIL_VERIFICATION)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        OtpEntity otpUser = otpRepository.save(otpEntity);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(createdUser.getEmail())
                .subject("Activate your account")
                .messageBody(String.format("""
                                                        Activate your account
                        Hi %s,
                        
                        Before you get started, confirm your email to activate your account.
                        Enter the OTP code for confirmation.
                                                        OTP : %s
                        
                        Copy and paste the OTP to activate your account
                        """, createdUser.getFirstName(), otpUser.getCode()))
                .build();
        emailService.sendEmailAlert(emailDetails);
        return ApiResponse.success(null,"Registration successful. Please check your email to verify your account.", 201);
    }

    @Override
    public ApiResponse<AuthResponse> verifyEmail(VerifyEmailRequest request) {
        if (!otpRepository.existsByEmail(request.getEmail())){
            ErrorDetail error = new ErrorDetail(ErrorCode.TOKEN_INVALID.name(), null);
            return ApiResponse.error("Token is invalid",error,401);
        }
        Optional<OtpEntity> otpOptional = otpRepository.findByCode(request.getOtp());
        if (otpOptional.isEmpty()){
            ErrorDetail error = new ErrorDetail(ErrorCode.TOKEN_INVALID.name(), null);
            return ApiResponse.error("Token is invalid",error,401);
        }

        OtpEntity otp = otpOptional.get();

        if (otp.isUsed()){
            ErrorDetail error = new ErrorDetail(ErrorCode.TOKEN_INVALID.name(), null);
            return ApiResponse.error("OTP has already been used",error,401);
        }

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            ErrorDetail error = new ErrorDetail(ErrorCode.TOKEN_EXPIRED.name(), null);
            return ApiResponse.error("OTP has expired. Please request a new one", error, 401);
        }

        if (otp.getType() != OtpType.EMAIL_VERIFICATION) {
            ErrorDetail error = new ErrorDetail(ErrorCode.TOKEN_INVALID.name(), null);
            return ApiResponse.error("Invalid OTP type", error, 401);
        }

        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            ErrorDetail error = new ErrorDetail(ErrorCode.USER_NOT_FOUND.name(), null);
            return ApiResponse.error("User not found", error, 404);
        }

        UserEntity user = userOptional.get();

        if (user.isEmailVerified()) {
            ErrorDetail error = new ErrorDetail(ErrorCode.EMAIL_TAKEN.name(), null);
            return ApiResponse.error("Email is already verified", error, 400);
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        user.setEmailVerified(true);
        userRepository.save(user);

        String accessToken  = jwtService.generateAccessToken((UserDetails) user);
        String refreshToken = jwtService.generateRefreshToken((UserDetails) user);

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900)
                .user(UserSummary.builder()
                        .id(String.valueOf(user.getId()))
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .kycTier(user.getKycTier())
                        .emailVerified(true)
                        .build())
                .build();

        return ApiResponse.success(authResponse, "Email verified successfully", 200);
    }

    @Override
    public ApiResponse<String> resendVerificationOtp(ResendVerificationRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            ErrorDetail error = new ErrorDetail(ErrorCode.USER_NOT_FOUND.name(), null);
            return ApiResponse.error("User not found", error, 404);
        }

        UserEntity user = userOptional.get();

        if (user.isEmailVerified()) {
            ErrorDetail error = new ErrorDetail(ErrorCode.EMAIL_TAKEN.name(), null);
            return ApiResponse.error("Email is already verified", error, 400);
        }

        Optional<OtpEntity> existingOtp = otpRepository.findByEmail(request.getEmail());
        existingOtp.ifPresent(otp -> {
            if (!otp.isUsed() && otp.getType() == OtpType.EMAIL_VERIFICATION) {
                otp.setUsed(true);
                otpRepository.save(otp);
            }
        });

        OtpEntity newOtp = OtpEntity.builder()
                .email(request.getEmail())
                .code(OTPGenerator.generateOTP())
                .type(OtpType.EMAIL_VERIFICATION)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();
        otpRepository.save(newOtp);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Verify your account - New OTP")
                .messageBody(String.format("""
            Verify your account
            
            Hi %s,
            
            You requested a new OTP to verify your email address.
            Enter the OTP code below to complete your verification.
            
            OTP: %s
            
            This OTP will expire in 10 minutes.
            
            If you didn't request this, please ignore this email.
            """, user.getFirstName(), newOtp.getCode()))
                .build();
        emailService.sendEmailAlert(emailDetails);

        return ApiResponse.success(null, "New verification OTP sent to your email", 200);
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        if (authenticate.isAuthenticated()){
            Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());
            if(userOptional.isPresent()){
                UserEntity user = userOptional.get();

                if(!user.isEmailVerified()){
                    ErrorDetail error = new ErrorDetail(ErrorCode.EMAIL_NOT_VERIFIED.name(),null);
                    return ApiResponse.error("Please verify your email before logging in", error, 403);
                }

                String accessToken  = jwtService.generateAccessToken((UserDetails) user);
                String refreshToken = jwtService.generateRefreshToken((UserDetails) user);

                UserSummary userSummary = UserSummary.builder()
                .id(String.valueOf(user.getId()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .kycTier(user.getKycTier())
                .emailVerified(user.isEmailVerified())
                .build();

                AuthResponse authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tokenType("jwt token")
                        .expiresIn(900)
                        .user(userSummary)
                        .build();
                return ApiResponse.success(authResponse,"Login Successful",200);
            }
        }
        ErrorDetail error = new ErrorDetail(ErrorCode.INVALID_CREDENTIALS.name(),null);
        return ApiResponse.error("Invalid email or password",error, 401);
    }
}
