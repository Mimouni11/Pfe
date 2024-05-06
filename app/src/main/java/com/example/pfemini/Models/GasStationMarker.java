package com.example.pfemini.Models;

import android.content.Context;

import com.example.pfemini.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class GasStationMarker extends Marker {

    public GasStationMarker(MapView mapView, Context context, double latitude, double longitude, String title) {
        super(mapView);
        setIcon(context.getResources().getDrawable(R.drawable.baseline_local_gas_station_24)); // Customize your gas station marker icon here
        setPosition(new GeoPoint(latitude, longitude));
        setTitle(title);
    }
}