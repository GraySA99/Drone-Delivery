package Mapping;

import java.util.Objects;

public class Waypoint {

    private double latitude;
    private double longitude;
    private boolean isStartingPoint;
    private String name;

    public Waypoint(String n, double latitude, double longitude, boolean isStarting) {
        latitude = latitude;
        longitude = longitude;
        isStartingPoint = isStarting;
        name = n;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
  
    public String getName() {
        return name;
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
        return longitude; // Josh - commented out - 4/1/20
    }

    public boolean isStarting() {
        return isStartingPoint;
    }
}
