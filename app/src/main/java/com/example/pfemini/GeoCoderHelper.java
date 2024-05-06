package com.example.pfemini;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoCoderHelper {

    public static GeoPoint getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context, Locale.getDefault());
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            return new GeoPoint(lat, lng);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
