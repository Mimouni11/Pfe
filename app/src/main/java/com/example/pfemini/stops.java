package com.example.pfemini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.circlemenu.CircleMenuView;
import com.google.maps.android.PolyUtil;

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

public class stops extends AppCompatActivity implements LocationListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MapView mapView;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        // Initialize MapView
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        mapView = findViewById(R.id.mapView);

        // Set the center of the map to Tunisia
        GeoPoint tunisiaCenter = new GeoPoint(34.0, 9.0);
        mapView.getController().setCenter(tunisiaCenter);

        // Set the initial zoom level to show Tunisia
        mapView.getController().setZoom(7.0);

        // Set the tile source to a source that focuses on Tunisia (e.g., OpenStreetMap Tunisia tile source)
        mapView.setTileSource(TileSourceFactory.MAPNIK); // You can replace this with a different tile source focusing on Tunisia

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
                        startActivity(new Intent(stops.this, restActivity.class));
                        break;
                    // Add more cases for additional buttons if needed

                    default:
                        break;
                }
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }


    }


    @Override
    public void onLocationChanged(Location location) {
        // Update current location marker on map
        GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        // Create a marker with a custom icon
        Marker marker = new Marker(mapView);
        marker.setPosition(currentLocation);
        marker.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_location_on_24));
        mapView.getOverlays().add(marker);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addMarker(GeoPoint location, String title) {
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle(title);
        marker.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_location_city_24));
        mapView.getOverlays().add(marker);
    }

    private void calculateRouteBetweenAddresses(List<String> addresses) {
        int size = addresses.size();
        if (size < 2) {
            Toast.makeText(this, "At least two addresses are required to calculate the route", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate route between consecutive pairs of addresses
        for (int i = 0; i < size - 1; i++) {
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
        // Construct the URL for the OpenRouteService API
        String baseUrl = "https://api.openrouteservice.org/v2/directions/driving-car";
        String apiKey = "5b3ce3597851110001cf6248c9ca2443f9b74caa9dd396ead2a6de27"; // Replace "YOUR_API_KEY" with your actual API key
        String url = baseUrl + "?api_key=" + apiKey +
                "&start=" + startLocation.getLongitude() + "," + startLocation.getLatitude() +
                "&end=" + endLocation.getLongitude() + "," + endLocation.getLatitude();

        // Execute AsyncTask to fetch route data
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
                urlConnection.setRequestProperty("Authorization", "5b3ce3597851110001cf6248c9ca2443f9b74caa9dd396ead2a6de27"); // Set your API key
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
                    routePolyline.setColor(Color.parseColor("#FF5722")); // Set polyline color
                    routePolyline.setWidth(10); // Set polyline width

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
                JSONObject jsonResponse = new JSONObject(routeData);
                JSONArray features = jsonResponse.getJSONArray("features");
                if (features.length() > 0) {
                    JSONObject feature = features.getJSONObject(0);
                    JSONObject geometry = feature.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    for (int i = 0; i < coordinates.length(); i++) {
                        JSONArray coord = coordinates.getJSONArray(i);
                        double lon = coord.getDouble(0);
                        double lat = coord.getDouble(1);
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


    private List<GeoPoint> simplifyPolyline(List<GeoPoint> polyline) {
        // Apply Douglas-Peucker algorithm to simplify the polyline
        // You can use libraries like JTS Topology Suite or implement the algorithm manually
        // Here's a simple implementation using a threshold distance
        double threshold = 0.0001; // Adjust threshold as needed
        List<GeoPoint> simplifiedPolyline = PolylineSimplification.simplify(polyline, threshold);
        return simplifiedPolyline;
    }

}