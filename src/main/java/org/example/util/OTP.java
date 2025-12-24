package org.example.util;

public class OTP {
    private final String code;
    private final long expiresAt;
    private boolean used;

    public OTP(String code, long expiresAt) {
        this.code = code;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() { return System.currentTimeMillis() > expiresAt; }

    public boolean isValid() { return !isExpired() && !used; }

    public void markUsed() { this.used = true; }
}
