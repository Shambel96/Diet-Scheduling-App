package com.example.dietapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String KEY_ROLE = "ROLE";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    // Save user session with role
    public void saveUser(int userId, String role) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Save default user session
    public void saveUser(int userId) {
        saveUser(userId, "user");
    }

    public String getRole() {
        return sp.getString(KEY_ROLE, "user"); // default to user
    }

    public boolean isAdmin() {
        return "admin".equals(getRole());
    }

    public boolean isLoggedIn() {
        return sp.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return sp.getInt(KEY_USER_ID, -1);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
