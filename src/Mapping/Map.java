package Mapping;

import java.util.ArrayList;

/**
 * Author: Patrick Reagan
 * Purpose: a data structure used by the simulation class for getting Waypoints. Stores a starting point and a list of other points.
 */

public class Map {
    private Waypoint startingPoint; //stores the starting point for the map
    private ArrayList<Waypoint> mapPoints; //stores the other Waypoints within a map
    private int size; //stores the size of the map

    /**
     * Creates a Map using a given starting point and ArrayList of Waypoints.
     * @param s is the given starting point.
     * @param list is the given list of other Waypoints.
     */
    public Map(Waypoint s, ArrayList<Waypoint> list){
        startingPoint = s;
        startingPoint.setStartingPoint(true);
        mapPoints = new ArrayList<Waypoint>();
        for(int i = 0; i < list.size(); i++){
            mapPoints.add(list.get(i));
            mapPoints.get(i).setStartingPoint(false);
        }
        size = mapPoints.size();
    }

    /**
     * Creates a Map using a given starting point and array of Waypoints.
     * @param s is the given starting point.
     * @param list is the given list of other Waypoints.
     */
    public Map(Waypoint s, Waypoint[] list){
        startingPoint = s;
        startingPoint.setStartingPoint(true);
        mapPoints = new ArrayList<Waypoint>();
        for(int i = 0; i < list.length; i++){
            mapPoints.add(list[i]);
            mapPoints.get(i).setStartingPoint(false);
        }
        size = mapPoints.size();
    }

    /**
     * Gives the starting point of the Map.
     * @return the map's starting point
     */
    public Waypoint getStartingPoint(){
        return startingPoint;
    }

    /**
     * Returns a Waypoint in the Map.
     * @param i the index of the requested Waypoint.
     * @return the ith index Waypoint.
     */
    public Waypoint getMapPoint(int i){
        return mapPoints.get(i);
    }

    /**
     * Returns the size of the other Waypoints array in the Map.
     * @return the size of the map.
     */
    public int getSize(){
        return size;
    }

    /**
     * Removes a specified Waypoint from the Map and recalculates its size.
     * @param i is the index of the Waypoint to be removed.
     */
    public void removeWaypoint(int i){
        mapPoints.remove(i);
        size = mapPoints.size();
    }
}

