package com.example.pfemini.Models;

public class Task {
    private String name;
    private boolean completed;
    private String mechanicName; // Add this field
    private String model; // Add model field
    private String matricule; // Add matricule field

    // Constructor with all fields
    public Task(String name, String model, String matricule) {
        this.name = name;
        this.model = model;
        this.matricule = matricule;
        this.completed = false; // Initialize as incomplete by default
    }

    // Other existing getters and setters...
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}
