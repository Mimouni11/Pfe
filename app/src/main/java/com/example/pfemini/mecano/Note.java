package com.example.pfemini.mecano;

public class Note {
    private String matricule;
    private String content;

    public Note() {}

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Note(String matricule, String content) {
        this.matricule = matricule;
        this.content = content;
    }
}