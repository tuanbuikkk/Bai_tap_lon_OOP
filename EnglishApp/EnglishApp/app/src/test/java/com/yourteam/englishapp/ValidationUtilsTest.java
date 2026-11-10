package com.yourteam.englishapp;

import static org.junit.Assert.*;

import com.yourteam.englishapp.utils.ValidationUtils;
import org.junit.Test;

public class ValidationUtilsTest {
    @Test public void email() {
        assertTrue(ValidationUtils.isValidEmail("a.b+c@mail.example.com"));
        assertFalse(ValidationUtils.isValidEmail("abc"));
        assertFalse(ValidationUtils.isValidEmail("a@b"));
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test public void password() {
        assertTrue(ValidationUtils.isValidPassword("abcdefg1"));
        assertFalse(ValidationUtils.isValidPassword("abcdefgh"));   // thiếu số
        assertFalse(ValidationUtils.isValidPassword("12345678"));   // thiếu chữ
        assertFalse(ValidationUtils.isValidPassword("ab1"));        // quá ngắn
    }

    @Test public void name() {
        assertTrue(ValidationUtils.isValidName("An"));
        assertFalse(ValidationUtils.isValidName(" A "));
        assertFalse(ValidationUtils.isValidName(null));
    }
}
