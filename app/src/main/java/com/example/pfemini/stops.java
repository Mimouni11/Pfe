package com.example.pfemini;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.circlemenu.CircleMenuView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class stops extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FloatingActionButton fabCenterLocation;

    private MyLocationNewOverlay mLocationOverlay;
   private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        // Initialize MapView
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);


        // Retrieve data from Intent extras
        Intent intent = getIntent();
        if (intent != null) {
            int childCount = intent.getIntExtra("childCount", 0); // Retrieve the total number of EditText fields
            List<String> addresses = intent.getStringArrayListExtra("addresses");
            if (addresses != null) {
                for (String address : addresses) {
                    // Convert EditText value to location GeoPoint
                    GeoPoint location = null;
                    try {
                        location = GeoCoderHelper.getLocationFromAddress(this, address);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    if (location != null) {
                        // Add marker for the location on the map
                        addMarker(location, address);
                    } else {
                        Toast.makeText(this, "Failed to find location for: " + address, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // Display the received data in a TextView
            StringBuilder displayText = new StringBuilder("Data from previous activity:\n");
            for (int i = 0; i < childCount; i++) {
                String editTextValue = intent.getStringExtra("editTextValue" + i);
                displayText.append("EditText Value ").append(i + 1).append(": ").append(editTextValue).append("\n");
            }
            // Display the addresses
            displayText.append("Addresses:\n");
            for (String address : addresses) {
                displayText.append(address).append("\n");
            }
            TextView textView = findViewById(R.id.textView);
            textView.setText(displayText.toString());



            centerOnUserLocation();


            // Retrieve latitude and longitude from intent extras




            // Create a GeoPoint for the user's location

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Permission already granted, enable location
                enableLocation();
                centerOnUserLocation();
            }


            fabCenterLocation = findViewById(R.id.fabCenterLocation);
            fabCenterLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerOnUserLocation();

                }
            });

        }









        // Display icons

        final CircleMenuView menu = findViewById(R.id.menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                switch (index) {
                    case 0:
                        // Button 0 clicked, navigate to ActivityA
                        startActivity(new Intent(stops.this, FuelingAct.class));
                        break;
                    case 1:
                        // Button 1 clicked, navigate to ActivityB
                        // Add your navigation code here
                        break;
                    default:
                        break;
                }
            }
        }); // Add this closing parenthesis
    }


    private void addMarker(GeoPoint location, String title) {
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle(title);
        mapView.getOverlays().add(marker);
        mapView.invalidate(); // Refresh the map view
    }


    private void centerOnUserLocation() {
        if (mLocationOverlay != null && mLocationOverlay.getMyLocation() != null) {
            GeoPoint myLocation = mLocationOverlay.getMyLocation();
            mapView.getController().animateTo(myLocation);
            mapView.getController().setZoom(18); // Example zoom level, adjust as needed
        } else {
            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
        }
    }


    private void enableLocation() {
        // Create and enable My Location overlay
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mLocationOverlay);
    }




}
