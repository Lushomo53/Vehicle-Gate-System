package org.gatesystem.util;

import java.security.*;
import java.util.Base64;

public class PasswordUtil {
    public static final String HASH_ALGORITHM = "SHA-256";
    public static final int SALT_LENGTH = 16;

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password) {
        byte[] salt = generateSalt();

        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt);

            byte[] passwordBytes = digest.digest(password.getBytes());

            String hashedPassword = Base64.getEncoder().encodeToString(passwordBytes);
            String hashedSalt = Base64.getEncoder().encodeToString(salt);

            return hashedPassword + ":" + hashedSalt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean checkPassword(String enteredPassword, String storedHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

            String[] parts = storedHash.split(":");

            byte[] salt = Base64.getDecoder().decode(parts[1]);
            digest.update(salt);

            byte[] passwordBytes = digest.digest(enteredPassword.getBytes());

            String hashedPassword = Base64.getEncoder().encodeToString(passwordBytes);

            return hashedPassword.equals(parts[0]);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error checking password", e);
        }
    }

}
