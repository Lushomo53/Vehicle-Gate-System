package org.example.util;

import java.security.SecureRandom;

public class OTPGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static OTP generate(int minutesValid) {
        String code = String.valueOf(100000 + RANDOM.nextInt(900000));
        long expiresAt = System.currentTimeMillis() + (minutesValid * 60_000L);
        return new OTP(code, expiresAt);
    }
}
