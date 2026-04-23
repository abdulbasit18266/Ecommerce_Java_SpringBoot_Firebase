package com.mobilestore.Abdulbasit.entity;

public class User {
    private String id; // Yeh Firestore document ke andar wali field se map hoga
    private String name;
    private String email;
    private String password;
    private String role;
    private String phoneNumber;

    // No-Args Constructor (Firestore ke liye zaroori hai)
    public User() {}

    // All-Args Constructor
    public User(String id, String name, String email, String password, String role, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}