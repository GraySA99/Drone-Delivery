package Simulation;

import Food.Order;
import Mapping.Waypoint;

import java.util.ArrayList;

public class Drone {
    private ArrayList<Order> loadedOrdersList = new ArrayList<Order>();
    private double weightCapacity;
    private Waypoint currentPosition, targetPosition;

    public Order getOrderOnDrone(int i){
        return loadedOrdersList.get(i);
    }

    public double getWeightCapacity(){
        return weightCapacity;
    }

    public Waypoint getCurrentPosition(){
        return currentPosition;
    }

    public Waypoint getTargetPosition(){
        return targetPosition;
    }

}
