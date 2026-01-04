package com.example.dietapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietapp.adapter.UserAdapter;
import com.example.dietapp.database.DatabaseHelper;
import com.example.dietapp.model.User;
import com.example.dietapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    RecyclerView recyclerUsers;
    DatabaseHelper db;
    SessionManager session;
    List<User> userList;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        // Option 3: light background → dark icons
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // ✅ dark icons
        );
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);

        // Initialize session
        session = new SessionManager(this);


        // Security check: only admins allowed
        if (!session.isAdmin()) {
            Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // RecyclerView setup
        recyclerUsers = findViewById(R.id.recyclerUsers);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList);
        recyclerUsers.setAdapter(adapter);

        adapter = new UserAdapter(this, userList);
        recyclerUsers.setAdapter(adapter);
        // Load all users
        loadUsers();
    }

    // Load users from database
    private void loadUsers() {
        userList.clear();

        Cursor cursor = db.getAllUsers();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                userList.add(new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("role"))
                ));
            } while (cursor.moveToNext());
        }

        if (cursor != null) cursor.close();

        adapter.notifyDataSetChanged();

        if (userList.isEmpty()) {
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
        }
    }

    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main_menu, menu);
        return true;
    }

    // Handle menu actions
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {

            // Show logout confirmation
            new AlertDialog.Builder(this)
                    .setTitle("Logout Confirmation")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        session.logout();
                        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Optional: refresh users from adapter
    public void refreshUsers() {
        loadUsers();
    }
}
