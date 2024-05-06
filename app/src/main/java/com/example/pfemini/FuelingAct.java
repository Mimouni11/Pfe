package com.example.pfemini;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pfemini.Models.GasStationMarker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FuelingAct extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MapView mapView;
    private MyLocationNewOverlay mLocationOverlay;
    private FloatingActionButton fabCenterLocation;
    private DatabaseReference userLocationRef;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fueling);

        // Initialize Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        userLocationRef = FirebaseDatabase.getInstance().getReference().child("sharedLocations").child("driver_locations");

        // Initialize the map view
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set the center of the map (e.g., Paris, France)
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapView.getController().setCenter(startPoint);
        mapView.getController().setZoom(12); // Set initial zoom level

        // Check and request location permission if not granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, enable location
            enableLocation();
            fetchNearbyGasStations();
        }

        // Initialize the FloatingActionButton
        fabCenterLocation = findViewById(R.id.fabCenterLocation);
        fabCenterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerOnUserLocation();
                updateLocationInDatabase();
                fetchNearbyGasStations();

            }
        });

        // Get username from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location
                enableLocation();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }



    private void fetchNearbyGasStations() {
        if (mLocationOverlay != null && mLocationOverlay.getMyLocation() != null) {
            GeoPoint userLocation = mLocationOverlay.getMyLocation();
            // Show toast message to indicate that the method is being executed
            Toast.makeText(this, "Fetching nearby gas stations...", Toast.LENGTH_SHORT).show();
            Log.d("location", String.valueOf(userLocation));
            // Prepare Overpass query to find nearby gas stations
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://overpass-api.de/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of the OverpassApiService interface
            OverpassApiService service = retrofit.create(OverpassApiService.class);

            // Prepare Overpass query to find nearby gas stations
            String query = "[out:json];" +
                    "node[amenity=fuel](around:5000," + userLocation.getLatitude() + "," + userLocation.getLongitude() + ");" +
                    "out;";

            // Make the API call using the service instance
            Call<JsonObject> call = service.searchForGasStations(query, "json");
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        // Show toast message to indicate successful response
                        Toast.makeText(FuelingAct.this, "Gas stations data received successfully", Toast.LENGTH_SHORT).show();
                        // Handle successful response
                        Log.d("response", String.valueOf(response));
                        JsonObject jsonObject = response.body();
                        try {
                            JsonArray elements = jsonObject.getAsJsonArray("elements");
                            for (int i = 0; i < elements.size(); i++) {
                                JsonObject element = elements.get(i).getAsJsonObject();
                                JsonObject tags = element.getAsJsonObject("tags");
                                String name = tags.has("name") ? tags.get("name").getAsString() : "Gas Station";
                                double latitude = element.get("lat").getAsDouble();
                                double longitude = element.get("lon").getAsDouble();
                                // Create a GeoPoint for the gas station location
                                GeoPoint gasStationLocation = new GeoPoint(latitude, longitude);
                                // Add a marker to the map for the gas station
                                addGasStationMarker(gasStationLocation, name);
                            }
                        } catch (JsonParseException e) {
                            Log.e("JSON Error", "Failed to parse JSON response", e);
                            // Show toast message for JSON parsing error
                            Toast.makeText(FuelingAct.this, "Failed to parse JSON response", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Show toast message for unsuccessful response
                        Toast.makeText(FuelingAct.this, "Failed to fetch data: " + response.message(), Toast.LENGTH_SHORT).show();
                        // Handle error
                        Log.e("API Error", "Failed to fetch data: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    // Show toast message for failure
                    Toast.makeText(FuelingAct.this, "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    // Handle failure
                    Log.e("API Error", "Failed to fetch data: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
        }
    }

    private void addGasStationMarker(GeoPoint gasStationLocation, String gasStationName) {
        Drawable icon = getResources().getDrawable(R.drawable.baseline_local_gas_station_24); // Replace with your custom icon
        Marker marker = new Marker(mapView);
        marker.setPosition(gasStationLocation);
        marker.setTitle(gasStationName);
        marker.setIcon(icon);
        mapView.getOverlays().add(marker);
    }









    private void enableLocation() {
        // Create and enable My Location overlay
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        mLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mLocationOverlay);
    }

    private void centerOnUserLocation() {
        if (mLocationOverlay != null && mLocationOverlay.getMyLocation() != null) {
            GeoPoint myLocation = mLocationOverlay.getMyLocation();
            mapView.getController().animateTo(myLocation);
        } else {
            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationInDatabase() {
        if (username != null && mLocationOverlay != null && mLocationOverlay.getMyLocation() != null) {
            GeoPoint myLocation = mLocationOverlay.getMyLocation();
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            userLocationRef.child(username).child("latitude").setValue(latitude);
            userLocationRef.child(username).child("longitude").setValue(longitude)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("LocationUpdate", "Location updated successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("LocationUpdate", "Failed to update location", e);
                        }
                    });
        } else {
            Toast.makeText(this, "Unable to update location", Toast.LENGTH_SHORT).show();
        }
    }
}
