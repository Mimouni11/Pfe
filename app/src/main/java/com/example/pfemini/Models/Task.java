package com.example.pfemini.Models;

public class Task {
    private String name;
    private boolean completed;
    private String mechanicName;
    private String model;
    private String matricule;
    private String taskType;

    // Constructor with all fields
    public Task(String name, String matricule, String taskType) {
        this.name = name;
        this.matricule = matricule;
        this.taskType = taskType;
        this.completed = false;
    }

    public Task(String taskName, String model, String matricule, String taskType) {
        this.name = taskName;
        this.model=model;
        this.matricule = matricule;
        this.taskType = taskType;

        this.completed = false;

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

    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
