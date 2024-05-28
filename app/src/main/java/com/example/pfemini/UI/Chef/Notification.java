package com.example.pfemini.UI.Chef;

public class Notification {
    private String title;
    private String content;
    // Add more fields as needed

    public Notification(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Add getter and setter methods for other fields
}
