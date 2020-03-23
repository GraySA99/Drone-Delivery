package Simulation;

import Food.Order;
import Food.OrderGenerator;
import Mapping.Waypoint;

import java.util.ArrayList;

public class Drone {
    private ArrayList<Order> loadedOrdersList = new ArrayList<Order>();
    private double weightCapacity;
    private Waypoint currentPosition, targetPosition;

    //Create array for delivery times with the sum of the number of orders in all of the shifts.
    OrderGenerator og = new OrderGenerator();
    int numShifts = og.numShifts;

    public double [] deliveryTimes = new double[];

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
