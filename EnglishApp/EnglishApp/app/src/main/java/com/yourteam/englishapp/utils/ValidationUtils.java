package com.yourteam.englishapp.utils;

import java.util.regex.Pattern;

/** Validate đầu vào. Không phụ thuộc Android để test được trên JVM. */
public final class ValidationUtils {
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");

    private ValidationUtils() {}

    public static boolean isValidEmail(String e) {
        return e != null && e.length() <= 150 && EMAIL.matcher(e.trim()).matches();
    }

    /** >= 8 ký tự, có chữ và số. */
    public static boolean isValidPassword(String p) {
        return p != null && p.length() >= 8 && p.length() <= 100
                && p.matches(".*[A-Za-z].*") && p.matches(".*\\d.*");
    }

    public static boolean isValidName(String n) {
        return n != null && n.trim().length() >= 2 && n.trim().length() <= 100;
    }
}
