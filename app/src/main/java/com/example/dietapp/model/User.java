package com.example.dietapp.model;

public class User {

    public int id;
    public String username;
    public String email;
    public String role;

    public User(int id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
