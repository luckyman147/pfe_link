package com.pfelink.monolith.infrastructure.security.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.UUID;

@Service
public class OtpService {
    private static final String DIGITS = "0123456789";
    private final SecureRandom random = new SecureRandom();

    public String generateOtp(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }

    public String generateEmailVerificationToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

