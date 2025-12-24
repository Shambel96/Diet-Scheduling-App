package com.example.dietapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.R;
import com.example.dietapp.database.DatabaseHelper;
import com.example.dietapp.utils.SessionManager;

public class AddFoodActivity extends AppCompatActivity {

    EditText etFoodName, etCalorie, etGram, etDate;
    Spinner spType;
    Button btnSave;

    DatabaseHelper db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Initialize views
        etFoodName = findViewById(R.id.etFoodName);
        etCalorie = findViewById(R.id.etCalorie);
        etGram = findViewById(R.id.etGram);
        etDate = findViewById(R.id.etDate);
        spType = findViewById(R.id.spType);
        btnSave = findViewById(R.id.btnSave);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Spinner values
        String[] types = {"Protein", "Vitamin", "Carbohydrate", "Fat"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types
        );
        spType.setAdapter(adapter);

        // Save button click
        btnSave.setOnClickListener(v -> {
            String name = etFoodName.getText().toString().trim();
            String calorieStr = etCalorie.getText().toString().trim();
            String gramStr = etGram.getText().toString().trim();
            String type = spType.getSelectedItem().toString();
            String date = etDate.getText().toString().trim();

            // Validation
            if (name.isEmpty() || calorieStr.isEmpty() || gramStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int calorie = Integer.parseInt(calorieStr);
            int gram = Integer.parseInt(gramStr);
            int userId = session.getUserId();

            boolean inserted = db.insertFood(
                    userId,
                    name,
                    calorie,
                    gram,
                    type,
                    date
            );

            if (inserted) {
                Toast.makeText(this, "Food added successfully", Toast.LENGTH_SHORT).show();
                finish(); // return to food list
            } else {
                Toast.makeText(this, "Failed to add food", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
