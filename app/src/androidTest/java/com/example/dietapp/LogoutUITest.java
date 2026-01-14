package com.example.dietapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LogoutUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFullLogoutFlow() {
        // 1. Click the Menu Icon (btnIcon)
        onView(withId(R.id.btnIcon)).perform(click());

        // 2. Click "Logout" inside the custom Dialog menu
        // We use withText because it's a TextView in dialog_menu
        onView(withText("Logout")).perform(click());

        // 3. The Confirmation Alert appears. Click "Yes"
        // Standard Android AlertDialogs use android.R.id.button1 for positive buttons
        onView(withText("Yes")).perform(click());

        // 4. Verify we are redirected to LoginActivity
        // Check if the login button from activity_login is now visible
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }
}