package Mapping;

public class Waypoint {

    private double latitude;
    private double longitude;
    private boolean isStartingPoint;

    public Waypoint(double latitude, double longitude, boolean isStarting) {
        this.latitude = latitude;
        this.longitude = longitude;
        isStartingPoint = isStarting;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isStarting() {
        return isStartingPoint;
    }

}
