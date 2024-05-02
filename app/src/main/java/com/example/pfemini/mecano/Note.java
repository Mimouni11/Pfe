package com.example.test2;

public class Note {
    private String Matricule;
    private String content;

    public Note() {

    }
    public String getMatricule() {
        return Matricule;
    }

    public void setMatricule(String title) {
        this.Matricule = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Note(String title, String content) {
        this.Matricule = title;
        this.content = content;
    }
}