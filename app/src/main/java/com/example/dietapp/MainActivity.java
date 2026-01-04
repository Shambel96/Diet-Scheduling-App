package com.example.dietapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietapp.R;
import com.example.dietapp.adapter.FoodAdapter;
import com.example.dietapp.database.DatabaseHelper;
import com.example.dietapp.model.Food;
import com.example.dietapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerFood;
    Button btnAddFood;

    DatabaseHelper db;
    SessionManager session;
    List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // or activity_admin

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        // Option 3: light background → dark icons
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // ✅ dark icons
        );
        insertAdminIfNotExists();

        setContentView(R.layout.activity_main);
        ImageButton btnIcon = findViewById(R.id.btnIcon);


        btnIcon.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_menu);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT)
                );
            }

            TextView btnLogout = dialog.findViewById(R.id.btnLogout);
            TextView btnAbout = dialog.findViewById(R.id.btnAbout);

            btnLogout.setOnClickListener(view -> {
                dialog.dismiss(); // close menu
                showLogoutConfirmation();
            });

            btnAbout.setOnClickListener(view -> {
                dialog.dismiss();
                showAboutDialog(); // if you already created about dialog
            });

            dialog.show();
        });




        recyclerFood = findViewById(R.id.recyclerFood);
        btnAddFood = findViewById(R.id.btnAddFood);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        foodList = new ArrayList<>();

        recyclerFood.setLayoutManager(new LinearLayoutManager(this));

        btnAddFood.setOnClickListener(v ->
                startActivity(new Intent(this, AddFoodActivity.class))
        );
    }

    private void showLogoutConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    session.logout();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_about);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
        }

        Button btnClose = dialog.findViewById(R.id.btnCloseDialog);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void insertAdminIfNotExists() {
        DatabaseHelper db = new DatabaseHelper(this);

        // Check if admin already exists
        Cursor cursor = db.getReadableDatabase().rawQuery(
                "SELECT * FROM users WHERE role='admin'", null);

        if (cursor.getCount() == 0) {
            // Insert admin
            boolean inserted = db.registerUser("Admin", "admin@gmail.com", "admin123", "admin");

            if (inserted) {
                Toast.makeText(this, "Admin account created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to create admin", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Admin already exists", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoodData();
    }

    private void loadFoodData() {
        foodList.clear();
        int userId = session.getUserId();

        Cursor cursor = db.getFoodByUser(userId);

        while (cursor.moveToNext()) {
            foodList.add(new Food(
                    cursor.getInt(0),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6)
            ));
        }

        cursor.close();

        FoodAdapter adapter = new FoodAdapter(this, foodList);
        recyclerFood.setAdapter(adapter);
    }
}
