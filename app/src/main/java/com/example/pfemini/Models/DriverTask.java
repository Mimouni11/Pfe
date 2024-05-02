package com.example.pfemini.Models;

public class DriverTask {
    private String task;
    private String matricule;

    public DriverTask(String task, String matricule) {
        this.task = task;
        this.matricule = matricule;
    }

    public String getTask() {
        return task;
    }

    public String getMatricule() {
        return matricule;
    }
}
