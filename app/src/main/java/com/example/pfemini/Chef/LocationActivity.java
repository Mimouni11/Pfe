package com.example.pfemini.Chef;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pfemini.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MapView mapView;
    private FloatingActionButton fabCenterLocation;
    private DatabaseReference userLocationsRef; // Reference to the "driver_locations" node

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Initialize Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        userLocationsRef = FirebaseDatabase.getInstance().getReference().child("sharedLocations").child("driver_locations");

        // Initialize the map view
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set initial zoom level
        mapView.getController().setZoom(12);

        // Fetch and display user locations
        fetchAndDisplayUserLocations();

        fabCenterLocation = findViewById(R.id.fabCenterLocation);
        fabCenterLocation.setOnClickListener(view -> {
            // No need to center on user's location since we are already centered on the specified user's location
            Toast.makeText(LocationActivity.this, "Already centered on user's location", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void fetchAndDisplayUserLocations() {
        userLocationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userName = userSnapshot.getKey(); // Get the username
                    if (userName != null) {
                        double latitude = userSnapshot.child("latitude").getValue(Double.class);
                        double longitude = userSnapshot.child("longitude").getValue(Double.class);
                        if ( latitude != 0.0 && longitude != 0.0) {                            GeoPoint userLocation = new GeoPoint(latitude, longitude);
                            addMarker(userLocation);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LocationActivity.this, "Failed to retrieve user locations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMarker(GeoPoint userLocation) {
        Drawable icon = getResources().getDrawable(R.drawable.baseline_location_on_24); // Customize your marker icon here
        OverlayItem overlayItem = new OverlayItem("User Location", "User Location", userLocation);
        overlayItem.setMarker(icon);
        ArrayList<OverlayItem> overlayItems = new ArrayList<>();
        overlayItems.add(overlayItem);
        ItemizedIconOverlay<OverlayItem> userLocationOverlay = new ItemizedIconOverlay<>(overlayItems, icon, null, this);
        mapView.getOverlays().add(userLocationOverlay);
    }
}
