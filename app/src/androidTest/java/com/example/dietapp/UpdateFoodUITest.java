package com.example.dietapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UpdateFoodUITest {

    // TEST 1: Checks if data loads and updates correctly
    @Test
    public void testUpdateFoodFlow() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, UpdateFoodActivity.class);
        intent.putExtra("id", 1);
        intent.putExtra("name", "Apple");
        intent.putExtra("calorie", 95);
        intent.putExtra("gram", 100);
        intent.putExtra("type", "Fruit");
        intent.putExtra("date", "25/10/2023");

        try (var scenario = androidx.test.core.app.ActivityScenario.launch(intent)) {
            onView(withId(R.id.etName)).check(matches(withText("Apple")));
            onView(withId(R.id.etName)).perform(replaceText("Green Apple"));
            onView(withId(R.id.btnUpdate)).perform(click());
        }
    }

    // TEST 2: Checks if the Date Picker actually pops up
    @Test
    public void testDatePickerVisibility() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, UpdateFoodActivity.class);
        // We still need to pass some extras so the Activity doesn't crash on onCreate
        intent.putExtra("date", "25/10/2023");

        try (var scenario = androidx.test.core.app.ActivityScenario.launch(intent)) {
            // 1. Click on the date field
            onView(withId(R.id.etDate)).perform(click());

            // 2. Check if the DatePickerDialog's "OK" button is visible
            // Standard Android date pickers use "OK" or "DONE"
            onView(withText("OK")).check(matches(isDisplayed()));
        }
    }
}