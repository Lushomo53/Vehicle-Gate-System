package org.gatesystem.util;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PasswordGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{}<>?";

    private static final int PASSWORD_LENGTH = 8;

    private PasswordGenerator() {
        // utility class – prevent instantiation
    }

    public static String generate() {
        List<Character> chars = new ArrayList<>();

        // ensure at least one of each required category
        chars.add(randomChar(UPPER));
        chars.add(randomChar(LOWER));
        chars.add(randomChar(DIGITS));
        chars.add(randomChar(SPECIAL));

        String allChars = UPPER + LOWER + DIGITS + SPECIAL;

        // fill remaining slots
        while (chars.size() < PASSWORD_LENGTH) {
            chars.add(randomChar(allChars));
        }

        // shuffle to remove predictability
        Collections.shuffle(chars, RANDOM);

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (char c : chars) {
            password.append(c);
        }

        return password.toString();
    }

    private static char randomChar(String source) {
        return source.charAt(RANDOM.nextInt(source.length()));
    }
}

