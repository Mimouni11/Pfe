package com.example.pfemini;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.webkit.WebView;

public class MyLocationListener implements LocationListener {
    private WebView webView;

    public MyLocationListener(WebView webView) {
        this.webView = webView;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Pass the location data to your WebView
        webView.loadUrl("javascript:updateMapLocation(" + location.getLatitude() + "," + location.getLongitude() + ")");
    }

    // Other overridden methods for LocationListener
}
