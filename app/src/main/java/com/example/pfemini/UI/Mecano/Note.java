package com.example.pfemini.UI.Mecano;

public class Note {
    private String matricule;
    private String content;
    private String workDescription;
    private String signature;

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

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Note(String matricule, String content, String workDescription, String signature) {
        this.matricule = matricule;
        this.content = content;
        this.workDescription = workDescription;
        this.signature = signature;
    }
}
