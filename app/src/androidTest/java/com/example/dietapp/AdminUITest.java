package com.example.dietapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminUITest {

    @Rule
    public ActivityScenarioRule<AdminActivity> activityRule =
            new ActivityScenarioRule<>(AdminActivity.class);

    @Test
    public void testUserListIsVisible() {
        // Verifies the RecyclerView is present on the screen
        onView(withId(R.id.recyclerUsers)).check(matches(isDisplayed()));
    }

    @Test
    public void testAdminLogoutFlow() {
        // 1. Open the Options Menu (the three dots / overflow menu)
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());

        // 2. Click on "Logout" from the menu
        // Note: Ensure your R.menu.admin_main_menu has an item with text "Logout"
        onView(withText("Logout")).perform(click());

        // 3. Verify the AlertDialog pops up with the correct Title
        onView(withText("Logout Confirmation")).check(matches(isDisplayed()));

        // 4. Click the "Yes" button in the Dialog
        onView(withText("Yes")).perform(click());

        // 5. Verify the app redirects to LoginActivity
        // We check for the existence of the login button (etEmail or btnLogin)
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }
}