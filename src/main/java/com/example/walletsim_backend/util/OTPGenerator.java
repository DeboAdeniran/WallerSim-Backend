package com.example.walletsim_backend.util;

import java.security.SecureRandom;

public class OTPGenerator {
    private static final SecureRandom random = new SecureRandom();
    public static String generateOTP(){
        int OTP_number = random.nextInt(0,999999);
        return ""+OTP_number;
    }
}
