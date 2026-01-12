package com.example.dietapp;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.Cursor;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.dietapp.database.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    private DatabaseHelper db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // We use a temporary database for testing
        db = new DatabaseHelper(context);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void testRegisterAndLoginUser() {
        // 1. Register a new user with 'admin' role
        boolean isRegistered = db.registerUser("TestAdmin", "test@admin.com", "pass123", "admin");
        assertTrue("User registration should return true", isRegistered);

        // 2. Try to login with those credentials
        Cursor cursor = db.loginUser("test@admin.com", "pass123");

        assertNotNull(cursor);
        assertTrue("Login should find at least one row", cursor.moveToFirst());

        // 3. Check if the role was saved correctly
        int roleIndex = cursor.getColumnIndex("role");
        assertEquals("admin", cursor.getString(roleIndex));
        cursor.close();
    }

    @Test
    public void testInsertAndGetFood() {
        // 1. Insert food for User ID 99
        boolean inserted = db.insertFood(99, "Apple", 95, 100, "Fruit", "2023-10-27");
        assertTrue(inserted);

        // 2. Retrieve food for User ID 99
        Cursor cursor = db.getFoodByUser(99);
        assertTrue(cursor.moveToFirst());

        int nameIndex = cursor.getColumnIndex("food_name");
        assertEquals("Apple", cursor.getString(nameIndex));
        cursor.close();
    }
}