package com.yourteam.englishapp.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test JVM thuần cho logic mật khẩu của ValidationUtils.
 * (isValidEmail dùng android.util.Patterns nên cần instrumented test.)
 */
public class ValidationUtilsTest {
    @Test
    public void validPassword() {
        assertTrue(ValidationUtils.isValidPassword("abc12345"));
    }

    @Test
    public void passwordTooShort() {
        assertFalse(ValidationUtils.isValidPassword("abc123"));
    }

    @Test
    public void passwordNoDigit() {
        assertFalse(ValidationUtils.isValidPassword("abcdefgh"));
    }

    @Test
    public void passwordNoLetter() {
        assertFalse(ValidationUtils.isValidPassword("12345678"));
    }

    @Test
    public void nameRules() {
        assertTrue(ValidationUtils.isValidName("An"));
        assertFalse(ValidationUtils.isValidName(" A "));
        assertFalse(ValidationUtils.isValidName(null));
    }
}
