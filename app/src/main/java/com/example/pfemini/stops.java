package com.example.pfemini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.circlemenu.CircleMenuView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class stops extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
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
            List<String> addresses = intent.getStringArrayListExtra("addresses");

            if (addresses != null && addresses.size() >= 2) {
                // Add markers for each address
                for (String address : addresses) {
                    GeoPoint location = GeoCoderHelper.getLocationFromAddress(this, address);
                    if (location != null) {
                        addMarker(location, address);
                    } else {
                        Toast.makeText(this, "Failed to find location for: " + address, Toast.LENGTH_SHORT).show();
                    }
                }

                // Calculate route between addresses
                calculateRouteBetweenAddresses(addresses);
            } else {
                Toast.makeText(this, "At least two addresses are required to calculate the route", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No addresses provided", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMarker(GeoPoint location, String title) {
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle(title);
        mapView.getOverlays().add(marker);
    }

    private void calculateRouteBetweenAddresses(List<String> addresses) {
        for (int i = 0; i < addresses.size() - 1; i++) {
            String startAddress = addresses.get(i);
            String endAddress = addresses.get(i + 1);

            GeoPoint startLocation = GeoCoderHelper.getLocationFromAddress(this, startAddress);
            GeoPoint endLocation = GeoCoderHelper.getLocationFromAddress(this, endAddress);

            if (startLocation != null && endLocation != null) {
                calculateRoute(startLocation, endLocation);
            } else {
                Toast.makeText(this, "Failed to calculate route between " + startAddress + " and " + endAddress, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void calculateRoute(GeoPoint startLocation, GeoPoint endLocation) {
        String url = "https://router.project-osrm.org/route/v1/driving/" +
                startLocation.getLongitude() + "," + startLocation.getLatitude() + ";" +
                endLocation.getLongitude() + "," + endLocation.getLatitude() +
                "?geometries=geojson";

        new FetchRouteTask().execute(url);
    }

    private class FetchRouteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            if (urls.length == 0) return null;

            String routeData = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                routeData = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return routeData;
        }

        @Override
        protected void onPostExecute(String routeData) {
            super.onPostExecute(routeData);
            if (routeData != null) {
                List<GeoPoint> routePoints = parseRouteData(routeData);

                if (routePoints != null && !routePoints.isEmpty()) {
                    Polyline routePolyline = new Polyline();
                    routePolyline.setPoints(routePoints);
                    routePolyline.setColor(Color.BLUE);
                    routePolyline.setWidth(5);

                    mapView.getOverlayManager().add(routePolyline);
                    mapView.invalidate();
                } else {
                    Toast.makeText(stops.this, "Failed to parse route data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(stops.this, "Failed to fetch route data", Toast.LENGTH_SHORT).show();
            }
        }

        private List<GeoPoint> parseRouteData(String routeData) {
            List<GeoPoint> routePoints = new ArrayList<>();
            try {
                JSONObject jsonRoute = new JSONObject(routeData);
                JSONArray jsonRoutes = jsonRoute.getJSONArray("routes");
                if (jsonRoutes.length() > 0) {
                    JSONObject jsonRouteObject = jsonRoutes.getJSONObject(0);
                    JSONObject jsonGeometry = jsonRouteObject.getJSONObject("geometry");
                    JSONArray jsonCoordinates = jsonGeometry.getJSONArray("coordinates");

                    for (int i = 0; i < jsonCoordinates.length(); i++) {
                        JSONArray jsonCoordinate = jsonCoordinates.getJSONArray(i);
                        double lon = jsonCoordinate.getDouble(0);
                        double lat = jsonCoordinate.getDouble(1);
                        GeoPoint geoPoint = new GeoPoint(lat, lon);
                        routePoints.add(geoPoint);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routePoints;
        }
    }
}
