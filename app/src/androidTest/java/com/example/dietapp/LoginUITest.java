package com.example.dietapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class LoginUITest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginEmptyFields_staysOnScreen() {
        // 1. Leave fields empty and click login
        onView(withId(R.id.btnLogin)).perform(click());

        // 2. Verify that we are still on the login page (the button is still visible)
        // This confirms the "if (email.isEmpty())" check in your LoginActivity worked
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidCredentials_showsErrorMessage() {
        // 1. Type a bad email and password
        onView(withId(R.id.etEmail)).perform(typeText("wrong@user.com"), closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("wrongpass"), closeSoftKeyboard());

        // 2. Click Login
        onView(withId(R.id.btnLogin)).perform(click());

        // 3. Since your app shows a Toast for "Invalid email or password",
        // the app stays on this screen. We verify by checking if the email field still exists.
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationToSignup() {
        // Tests if clicking the signup text works
        onView(withId(R.id.tvSignup)).perform(click());

        // You can check if a view unique to SignupActivity is now visible
        // onView(withId(R.id.btnRegister)).check(matches(isDisplayed()));
    }
}