package com.example.dietapp;

public class LoginValidator {

    // Logic: Fields shouldn't be empty
    public boolean isInputValid(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    // Logic: Basic email format check
    public boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}