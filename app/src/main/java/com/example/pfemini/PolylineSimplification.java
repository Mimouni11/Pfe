package com.example.pfemini;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class PolylineSimplification {

    // Function to simplify a polyline using the Douglas-Peucker algorithm
    public static List<GeoPoint> simplify(List<GeoPoint> polyline, double tolerance) {
        if (polyline == null || polyline.size() < 3) {
            return polyline;
        }

        // Find the point with the maximum distance
        double maxDistance = 0;
        int index = 0;
        int end = polyline.size() - 1;
        for (int i = 1; i < end; i++) {
            double distance = perpendicularDistance(polyline.get(i), polyline.get(0), polyline.get(end));
            if (distance > maxDistance) {
                index = i;
                maxDistance = distance;
            }
        }

        // If the maximum distance is greater than the tolerance, recursively simplify
        List<GeoPoint> result = new ArrayList<>();
        if (maxDistance > tolerance) {
            List<GeoPoint> firstPart = simplify(polyline.subList(0, index + 1), tolerance);
            List<GeoPoint> secondPart = simplify(polyline.subList(index, polyline.size()), tolerance);
            result.addAll(firstPart.subList(0, firstPart.size() - 1)); // Exclude the last point of the first part
            result.addAll(secondPart);
        } else {
            // Otherwise, return the start and end points
            result.add(polyline.get(0));
            result.add(polyline.get(end));
        }

        return result;
    }

    // Function to calculate the perpendicular distance from a point to a line segment
    private static double perpendicularDistance(GeoPoint point, GeoPoint start, GeoPoint end) {
        double x = point.getLongitude();
        double y = point.getLatitude();
        double x1 = start.getLongitude();
        double y1 = start.getLatitude();
        double x2 = end.getLongitude();
        double y2 = end.getLatitude();

        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
