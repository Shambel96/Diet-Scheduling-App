package com.example.dietapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

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
        setContentView(R.layout.activity_main);
        ImageButton btnIcon = findViewById(R.id.btnIcon);

        btnIcon.setOnClickListener(v -> {
            // Create dialog
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_about);
            dialog.setCancelable(true);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            // Close button
            Button btnClose = dialog.findViewById(R.id.btnCloseDialog);
            btnClose.setOnClickListener(view -> dialog.dismiss());

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
