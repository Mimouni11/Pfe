package com.example.pfemini.Models;

public class Tasks_driver {

    private String idtask;
    private String id_driver;
    private String date;
    private String task;



    public Tasks_driver(String idtask, String id_driver, String date, String task) {
        this.idtask = idtask;
        this.id_driver = id_driver;
        this.task = task;
        this.date = date;

    }
    public String getIdtask() {
        return idtask;
    }

    public void setIdtask(String idtask) {
        this.idtask = idtask;
    }

    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

}

