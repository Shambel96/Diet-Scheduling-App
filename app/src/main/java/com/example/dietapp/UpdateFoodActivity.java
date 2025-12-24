package com.example.dietapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.R;
import com.example.dietapp.database.DatabaseHelper;
public class UpdateFoodActivity extends AppCompatActivity {

    EditText etName, etCalorie, etGram, etType, etDate;
    Button btnUpdate;
    DatabaseHelper db;
    int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        etName = findViewById(R.id.etName);
        etCalorie = findViewById(R.id.etCalorie);
        etGram = findViewById(R.id.etGram);
        etType = findViewById(R.id.etType);
        etDate = findViewById(R.id.etDate);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = new DatabaseHelper(this);

        // Receive data
        foodId = getIntent().getIntExtra("id", -1);
        etName.setText(getIntent().getStringExtra("name"));
        etCalorie.setText(String.valueOf(getIntent().getIntExtra("calorie", 0)));
        etGram.setText(String.valueOf(getIntent().getIntExtra("gram", 0)));
        etType.setText(getIntent().getStringExtra("type"));
        etDate.setText(getIntent().getStringExtra("date"));

        btnUpdate.setOnClickListener(v -> {
            boolean updated = db.updateFood(
                    foodId,
                    etName.getText().toString(),
                    Integer.parseInt(etCalorie.getText().toString()),
                    Integer.parseInt(etGram.getText().toString()),
                    etType.getText().toString(),
                    etDate.getText().toString()
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
