package com.example.dietapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginValidatorTest {

    private final LoginValidator validator = new LoginValidator();

    @Test
    public void emptyEmail_returnsFalse() {
        boolean result = validator.isInputValid("", "password123");
        assertFalse("Empty email should be invalid", result);
    }

    @Test
    public void validEmailAndPassword_returnsTrue() {
        boolean result = validator.isInputValid("user@example.com", "password123");
        assertTrue(result);
    }

    @Test
    public void invalidEmailFormat_returnsFalse() {
        assertFalse(validator.isValidEmail("not-an-email"));
    }
}