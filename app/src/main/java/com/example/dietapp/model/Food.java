package com.example.dietapp.model;

public class Food {

    private int id;
    private final String name;
    private int calorie;
    private int gram;
    private String type;
    private String date;

    public Food(int id, String name, int calorie, int gram, String type, String date) {
        this.id = id;
        this.name = name;
        this.calorie = calorie;
        this.gram = gram;
        this.type = type;
        this.date = date;
    }

    // ✅ GETTERS
    public int getId() {
        return id;
    }

    public String getFoodName() {
        return name;
    }

    public int getCalorie() {
        return calorie;
    }

    public int getGram() {
        return gram;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
