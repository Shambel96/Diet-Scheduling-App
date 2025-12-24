package com.example.dietapp.utils;

import android.content.*;

public class SessionManager {

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sp = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveUser(int userId) {
        editor.putInt("USER_ID", userId);
        editor.apply();
    }

    public int getUserId() {
        return sp.getInt("USER_ID", -1);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
