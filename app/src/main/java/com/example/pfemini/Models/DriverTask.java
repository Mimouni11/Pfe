package com.example.pfemini.Models;

public class DriverTask {
    private String task;
    private String matricule;
    private boolean isSelected; // Add a field to store the selection state

    public DriverTask(String task, String matricule) {
        this.task = task;
        this.matricule = matricule;
        this.isSelected = false; // Initialize the selection state to false
    }

    public String getTask() {
        return task;
    }

    public String getMatricule() {
        return matricule;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
