package com.example.dietapp.database;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "diet.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // USER TABLE
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT," +
                "password TEXT)");

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
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS food");
        onCreate(db);
    }

    // ---------------- USER CRUD ----------------

    // SIGNUP
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);

        return db.insert("users", null, cv) != -1;
    }

    // LOGIN
    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );
    }

    // ---------------- FOOD CRUD ----------------

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

        int result = db.update("food",
                cv,
                "id = ?",
                new String[]{String.valueOf(id)});

        return result > 0;
    }


    public boolean deleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("food", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

}
