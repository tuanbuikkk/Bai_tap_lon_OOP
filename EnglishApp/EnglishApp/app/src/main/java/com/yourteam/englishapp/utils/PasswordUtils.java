package com.yourteam.englishapp.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Băm mật khẩu bằng PBKDF2WithHmacSHA256 + salt ngẫu nhiên.
 * Dùng mã hex (thay vì android.util.Base64) để chạy được cả unit test trên JVM.
 */
public final class PasswordUtils {
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LEN = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtils() {}

    /** Salt 16 byte, dạng hex (32 ký tự, vừa cột Salt NVARCHAR(64)). */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return toHex(salt);
    }

    /** Trả về hash dạng hex (64 ký tự, vừa cột PasswordHash). */
    public static String hash(String password, String saltHex) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), fromHex(saltHex), ITERATIONS, KEY_LEN);
            byte[] h = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
            return toHex(h);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /** So sánh thời gian hằng số để tránh timing attack. */
    public static boolean verify(String password, String saltHex, String expectedHash) {
        if (password == null || saltHex == null || expectedHash == null) return false;
        return MessageDigest.isEqual(
                hash(password, saltHex).getBytes(StandardCharsets.UTF_8),
                expectedHash.getBytes(StandardCharsets.UTF_8));
    }

    static String toHex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte x : b) sb.append(String.format("%02x", x));
        return sb.toString();
    }

    static byte[] fromHex(String s) {
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return out;
    }
}
