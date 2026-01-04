package com.example.dietapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietapp.AdminActivity;
import com.example.dietapp.R;
import com.example.dietapp.database.DatabaseHelper;
import com.example.dietapp.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final List<User> users;
    private final DatabaseHelper db;
    private final AdminActivity adminActivity;

    private final int SUPER_ADMIN_ID = 2; // Super admin ID


    public UserAdapter(AdminActivity activity, List<User> users) {
        this.context = activity;
        this.users = users;
        this.db = new DatabaseHelper(context);
        this.adminActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvUsername.setText(user.username);
        holder.tvEmail.setText(user.email);
        holder.tvRole.setText("Role: " + user.role);

        boolean isSuperAdmin = user.id == SUPER_ADMIN_ID;

        // Button colors
        int promoteColor = ContextCompat.getColor(context, R.color.green); // Promote
        int demoteColor = ContextCompat.getColor(context, R.color.orange); // Demote
        int deleteColor = ContextCompat.getColor(context, R.color.red);     // Delete
        int disabledColor = ContextCompat.getColor(context, R.color.gray);  // Disabled

        // Set button text
        holder.btnRole.setText(user.role.equals("user") ? "Promote" : "Demote");

        // Apply colors
        if (isSuperAdmin) {
            holder.btnRole.setBackgroundColor(disabledColor);
            holder.btnDelete.setBackgroundColor(disabledColor);
        } else {
            holder.btnRole.setBackgroundColor(user.role.equals("user") ? promoteColor : demoteColor);
            holder.btnDelete.setBackgroundColor(deleteColor);
        }

        // Enable/disable buttons
        holder.btnRole.setEnabled(!isSuperAdmin);
        holder.btnDelete.setEnabled(!isSuperAdmin);

        // Promote/Demote click
        holder.btnRole.setOnClickListener(v -> {
            String newRole = user.role.equals("user") ? "admin" : "user";
            if (db.updateUserRole(user.id, newRole)) {
                Toast.makeText(context, user.username + " is now " + newRole, Toast.LENGTH_SHORT).show();
                adminActivity.refreshUsers();
            } else {
                Toast.makeText(context, "Failed to update role", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete click
        holder.btnDelete.setOnClickListener(v -> {
            if (db.deleteUser(user.id)) {
                Toast.makeText(context, user.username + " deleted", Toast.LENGTH_SHORT).show();
                adminActivity.refreshUsers();
            } else {
                Toast.makeText(context, "Cannot delete super admin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvEmail, tvRole;
        Button btnRole, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnRole = itemView.findViewById(R.id.btnRole);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
