package com.example.dietapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.R;
import com.example.dietapp.database.DatabaseHelper;

import java.util.Calendar;

public class UpdateFoodActivity extends AppCompatActivity {

    EditText etName, etCalorie, etGram, etType, etDate;
    Button btnUpdate;
    DatabaseHelper db;
    int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        // Transparent status bar + dark icons
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );

        // Initialize views
        etName = findViewById(R.id.etName);
        etCalorie = findViewById(R.id.etCalorie);
        etGram = findViewById(R.id.etGram);
        etType = findViewById(R.id.etType);
        etDate = findViewById(R.id.etDate);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = new DatabaseHelper(this);

        // Receive data from intent
        foodId = getIntent().getIntExtra("id", -1);
        etName.setText(getIntent().getStringExtra("name"));
        etCalorie.setText(String.valueOf(getIntent().getIntExtra("calorie", 0)));
        etGram.setText(String.valueOf(getIntent().getIntExtra("gram", 0)));
        etType.setText(getIntent().getStringExtra("type"));
        etDate.setText(getIntent().getStringExtra("date"));

        // ================= DATE PICKER =================
        etDate.setInputType(0); // disables keyboard
        etDate.setFocusable(false); // prevent manual typing
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            // Try to prefill picker with current date in EditText
            String[] parts = etDate.getText().toString().split("/");
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (parts.length == 3) {
                try {
                    day = Integer.parseInt(parts[0]);
                    month = Integer.parseInt(parts[1]) - 1; // month is 0-indexed
                    year = Integer.parseInt(parts[2]);
                } catch (NumberFormatException ignored) {}
            }

            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, y, m, d) -> etDate.setText(d + "/" + (m + 1) + "/" + y),
                    year, month, day
            );
            datePicker.show();
        });

        // ================= UPDATE BUTTON =================
        btnUpdate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String calorieStr = etCalorie.getText().toString().trim();
            String gramStr = etGram.getText().toString().trim();
            String type = etType.getText().toString().trim();
            String date = etDate.getText().toString().trim();

            // Validation
            if (name.isEmpty() || calorieStr.isEmpty() || gramStr.isEmpty() || date.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = db.updateFood(
                    foodId,
                    name,
                    Integer.parseInt(calorieStr),
                    Integer.parseInt(gramStr),
                    type,
                    date
            );

            if (updated) {
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
