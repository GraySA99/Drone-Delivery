package Mapping;

import java.util.Objects;

/**
 * Authors: Patrick Reagan, Spencer Gray, and Josh Worley
 * Purpose: Used in the simulation for storing the locations of various delivery points.
 */

public class Waypoint {

    private double latitude; //stores the latitude of the delivery point
    private double longitude; //stores the longitude of the delivery point
    private boolean isStartingPoint; //stores true or false depending on is the Waypoint is a starting point or not
    private String name; //stores the name of the Waypoint

    /**
     * Creates a new Waypoint with a specified name, latitude, longitude, and whether or not it is a starting point.
     * @param n is the specified name of the Waypoint.
     * @param latitude is the specified latitude of the Waypoint.
     * @param longitude is the specified longitude of the Waypoint.
     * @param isStarting is whether or no the Waypoint is true or false;
     */
    public Waypoint(String n, double latitude, double longitude, boolean isStarting) {
        this.latitude = latitude;
        this.longitude = longitude;
        isStartingPoint = isStarting;
        name = n;
    }

    /**
     * Creates a new Waypoint with a specified name, latitude, longitude, but is automatically declared not a starting point.
     * @param n is the specified name of the Waypoint.
     * @param latitude is the specified latitude of the Waypoint.
     * @param longitude is the specified longitude of the Waypoint.
     */
    public Waypoint(String n, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        isStartingPoint = false;
        name = n;
    }

    /**
     * Returns the latitude of the Waypoint.
     * @return the latitude of the Waypoint.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the Longitude of the Waypoint.
     * @return the Longitude of the Waypoint.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the name of the Waypoint.
     * @return the name of the Waypoint.
     */
    public String getName() {
        return name;
    }

    /**\
     * The equality override for comparing Waypoints.
     * @param o is a specified Waypoint.
     * @return whether this Waypoint is equivalent to the other.
     */
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
        //return longitude;
    }

    /**
     * Returns true or false depending on if this Waypoint is a starting point.
     * @return true or false depending on if this Waypoint is a starting point.
     */
    public boolean isStarting() {
        return isStartingPoint;
    }

    /**
     * Sets the isStartingPoint value to a specified one.
     * @param b is the specified value for isStartingPoint.
     */
    public void setStartingPoint(boolean b){
        isStartingPoint = b;
    }
}
