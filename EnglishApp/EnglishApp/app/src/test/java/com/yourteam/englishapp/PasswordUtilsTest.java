package com.yourteam.englishapp;

import static org.junit.Assert.*;

import com.yourteam.englishapp.utils.PasswordUtils;
import org.junit.Test;

public class PasswordUtilsTest {
    @Test public void hashIsNotPlainText() {
        String salt = PasswordUtils.generateSalt();
        String h = PasswordUtils.hash("Abc12345", salt);
        assertNotEquals("Abc12345", h);
        assertTrue(h.length() <= 255);
        assertTrue(salt.length() <= 64);
    }

    @Test public void verifyCorrectAndWrongPassword() {
        String salt = PasswordUtils.generateSalt();
        String h = PasswordUtils.hash("Abc12345", salt);
        assertTrue(PasswordUtils.verify("Abc12345", salt, h));
        assertFalse(PasswordUtils.verify("abc12345", salt, h));
        assertFalse(PasswordUtils.verify(null, salt, h));
    }

    @Test public void sameSaltSameHashDifferentSaltDifferentHash() {
        String s1 = PasswordUtils.generateSalt();
        String s2 = PasswordUtils.generateSalt();
        assertNotEquals(s1, s2);
        assertEquals(PasswordUtils.hash("Abc12345", s1), PasswordUtils.hash("Abc12345", s1));
        assertNotEquals(PasswordUtils.hash("Abc12345", s1), PasswordUtils.hash("Abc12345", s2));
    }
}
