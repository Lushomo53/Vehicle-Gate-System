package org.example.util;

import java.util.regex.Pattern;

public class RegexPatterns {
    public static final Pattern NRC_PATTERN = Pattern.compile("^\\d{6}/\\d{2}/\\d$");
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final Pattern AT_LEAST_ONE_UPPERCASE = Pattern.compile(".*[A-Z].*");
    public static final Pattern AT_LEAST_ONE_LOWERCASE = Pattern.compile(".*[a-z].*");
    public static final Pattern AT_LEAST_ONE_SPECIAL = Pattern.compile(".*[!@$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
}
