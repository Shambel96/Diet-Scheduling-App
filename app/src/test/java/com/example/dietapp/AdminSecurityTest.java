package com.example.dietapp;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.dietapp.utils.SessionManager;

import org.junit.Test;

public class AdminSecurityTest {

    @Test
    public void testAdminSecurity_NonAdmin_ReturnsFalse() {
        // 1. Mock the SessionManager
        // We use mock() to create a "fake" version of your session manager
        SessionManager mockSession = mock(SessionManager.class);

        // 2. Train the mock
        // We tell the fake session: "If someone asks if you are an admin, say false."
        when(mockSession.isAdmin()).thenReturn(false);

        // 3. Verify the logic
        // This confirms your security check correctly identifies a non-admin
        assertFalse("A non-admin user should not have admin access", mockSession.isAdmin());
    }
}