package com.example.dietapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietapp.database.DatabaseHelper;
import com.example.dietapp.R;
import com.example.dietapp.UpdateFoodActivity;
import com.example.dietapp.model.Food;

import java.util.List;

// Correction 1: Removed 'abstract' keyword
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final Context context;
    private final List<Food> foodList;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    // Correction 2: Removed <DatabaseHelper> generic from the method signature
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Use holder.getAdapterPosition() for safety, as 'position' can become stale.
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition == RecyclerView.NO_POSITION) {
            return; // Invalid position, do nothing.
        }

        final Food food = foodList.get(currentPosition);

        holder.tvFoodName.setText(food.getFoodName());
        holder.tvDetails.setText(
                "Calories: " + food.getCalorie() +
                        " | Gram: " + food.getGram() +
                        "\nType: " + food.getType() +
                        " | Date: " + food.getDate()
        );

        // 👉 CLICK TO UPDATE
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateFoodActivity.class);
            intent.putExtra("id", food.getId());
            intent.putExtra("name", food.getFoodName());
            intent.putExtra("calorie", food.getCalorie());
            intent.putExtra("gram", food.getGram());
            intent.putExtra("type", food.getType());
            intent.putExtra("date", food.getDate());
            context.startActivity(intent);
        });

        // 👉 DELETE BUTTON
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Food")
                    .setMessage("Are you sure you want to delete this food?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.deleteFood(food.getId()); // Assumes 'deleteFood' is the correct method name

                        // Safely remove the item from the list and notify the adapter
                        int itemPosition = holder.getAdapterPosition();
                        if (itemPosition != RecyclerView.NO_POSITION) {
                            foodList.remove(itemPosition);
                            notifyItemRemoved(itemPosition);
                            // It's often better not to call notifyItemRangeChanged right after remove
                            // notifyItemRemoved is usually sufficient.
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodName, tvDetails;
        Button btnDelete;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
