package com.example.dietapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.database.DatabaseHelper;
import com.example.dietapp.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvSignup;

    DatabaseHelper db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // or activity_admin

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        // Option 3: light background → dark icons
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // ✅ dark icons
        );
        session = new SessionManager(this);

        // Already logged in? Redirect to proper page
        if (session.isLoggedIn()) {
            redirectByRole(session.getRole());
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = db.loginUser(email, password);
            if (cursor != null && cursor.moveToFirst()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));

                session.saveUser(userId, role);

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                redirectByRole(role);

                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) cursor.close();
        });

        tvSignup.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }

    private void redirectByRole(String role) {
        if ("admin".equals(role)) {
            startActivity(new Intent(this, AdminActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
