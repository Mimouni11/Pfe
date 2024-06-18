package com.example.pfemini.Models;

public class LoginResponse {
    private String status;
    private String message;
    private String role;
    private String type; // Add this field

    // Getters and setters for the fields
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type; // Add this getter
    }

    public void setType(String type) {
        this.type = type; // Add this setter
    }
}
