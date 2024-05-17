package com.example.pfemini.Models;

public class Task {
    private String name;
    private boolean completed;
    private String mechanicName; // Add this field

    public Task(String name) {
        this.name = name;
        this.completed = false; // Initialize as incomplete by default
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getMechanicName() { // Add this getter method
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) { // Add this setter method
        this.mechanicName = mechanicName;
    }
}
