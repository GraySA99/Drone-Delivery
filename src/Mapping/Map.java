package Mapping;

import java.util.ArrayList;

public class Map {
    private Waypoint startingPoint;
    private ArrayList<Waypoint> mapPoints;
    private int size;

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

    public Waypoint getStartingPoint(){
        return startingPoint;
    }

    public Waypoint getMapPoint(int i){
        return mapPoints.get(i);
    }

    public int getSize(){
        return size;
    }
}

