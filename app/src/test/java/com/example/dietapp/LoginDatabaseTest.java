package com.example.dietapp;

import android.database.Cursor;
import com.example.dietapp.database.DatabaseHelper;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginDatabaseTest {

    @Test
    public void loginUser_withValidCredentials_returnsData() {
        // 1. Create a "Mock" (fake) version of your Database and Cursor
        DatabaseHelper mockDb = mock(DatabaseHelper.class);
        Cursor mockCursor = mock(Cursor.class);

        // 2. "Train" the mock: When loginUser is called, return our fake cursor
        when(mockDb.loginUser("test@email.com", "password123")).thenReturn(mockCursor);

        // 3. "Train" the cursor: Pretend it found a row in the database
        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.getInt(anyInt())).thenReturn(1); // Return User ID 1
        when(mockCursor.getString(anyInt())).thenReturn("admin"); // Return role "admin"

        // 4. Execute the test
        Cursor result = mockDb.loginUser("test@email.com", "password123");

        // 5. Assertions: Verify it works as expected
        assertNotNull(result);
        assertTrue(result.moveToFirst());
        assertEquals("admin", result.getString(0));
    }
}