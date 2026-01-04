package com.example.dietapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "diet.db";
    private static final int DB_VERSION = 2; // ⬅ upgraded for role support

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // USERS TABLE (WITH ROLE)
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "role TEXT DEFAULT 'user')");

        // FOOD TABLE
        db.execSQL("CREATE TABLE food (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "food_name TEXT," +
                "calorie INTEGER," +
                "gram INTEGER," +
                "type TEXT," +
                "date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Safe upgrade (keeps old data)
        if (oldV < 2) {
            db.execSQL("ALTER TABLE users ADD COLUMN role TEXT DEFAULT 'user'");
        }
    }

    // ================= USER METHODS =================

    // REGISTER USER
    public boolean registerUser(String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("role", role); // user / admin

        return db.insert("users", null, cv) != -1;
    }

    // LOGIN USER
    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT id, username, email, role FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );
    }


    // GET ALL USERS (ADMIN DASHBOARD)
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT id, username, email, role FROM users",
                null
        );
    }

    // UPDATE USER ROLE (PROMOTE / DEMOTE)
    public boolean updateUserRole(int userId, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("role", role);

        return db.update(
                "users",
                cv,
                "id = ?",
                new String[]{String.valueOf(userId)}
        ) > 0;
    }

    // DELETE USER (OPTIONAL)
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(
                "users",
                "id = ?",
                new String[]{String.valueOf(userId)}
        ) > 0;
    }

    // ================= FOOD METHODS =================

    public boolean insertFood(int userId, String name, int calorie, int gram, String type, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("user_id", userId);
        cv.put("food_name", name);
        cv.put("calorie", calorie);
        cv.put("gram", gram);
        cv.put("type", type);
        cv.put("date", date);

        return db.insert("food", null, cv) != -1;
    }

    public Cursor getFoodByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM food WHERE user_id=?",
                new String[]{String.valueOf(userId)}
        );
    }

    public boolean updateFood(int id, String name, int calorie, int gram, String type, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("food_name", name);
        cv.put("calorie", calorie);
        cv.put("gram", gram);
        cv.put("type", type);
        cv.put("date", date);

        return db.update(
                "food",
                cv,
                "id = ?",
                new String[]{String.valueOf(id)}
        ) > 0;
    }

    public boolean deleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(
                "food",
                "id = ?",
                new String[]{String.valueOf(id)}
        ) > 0;
    }
}
