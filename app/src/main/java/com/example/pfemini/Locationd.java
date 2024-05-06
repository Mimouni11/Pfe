package com.example.pfemini;

public class Locationd {
    private double latitude;
    private double longitude;

    public Locationd() {
        // Default constructor required for Firebase
    }

    public Locationd(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
