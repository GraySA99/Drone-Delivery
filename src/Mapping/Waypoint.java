package Mapping;

import java.util.Objects;

public class Waypoint {

    private String name;
    private double latitude;
    private double longitude;

    public Waypoint(String n, double lat, double lng) {

        name = n;
        latitude = lat;
        longitude = lng;
    }

    public String getName() {

        return name;
    }

    public double getLatitude() {

        return latitude;
    }

    public double getLongitude() {

        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waypoint waypoint = (Waypoint) o;
        return Double.compare(waypoint.latitude, latitude) == 0 &&
                Double.compare(waypoint.longitude, longitude) == 0 &&
                Objects.equals(name, waypoint.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, latitude, longitude);
    }
}
