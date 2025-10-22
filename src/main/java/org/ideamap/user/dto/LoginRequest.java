package org.ideamap.user.dto;

// A simple Plain Old Java Object (POJO) to represent the login request body
public class LoginRequest {
    private String username;
    private String password;

    // Getters and setters are required for JSON deserialization
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}