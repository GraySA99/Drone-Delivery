package Simulation;

import Food.Order;
import Mapping.Waypoint;

import java.util.ArrayList;

public class Drone {
    private ArrayList<Order> loadedOrdersList;
    private double weightCapacity;
    private Waypoint currentPosition;
    private ArrayList<Waypoint> targetPositions;
    private int turnAroundTime;
    public ArrayList<Double> deliveryTimes;

    public Drone(){ //for a default drone
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        //waypoint at start
        targetPositions = new ArrayList<Waypoint>();
        turnAroundTime = 3;
        deliveryTimes = new ArrayList<Double>();
    }

    //Create array for delivery times with the sum of the number of orders in all of the shifts.
    //OrderGenerator og = new OrderGenerator();
    //int totalOrders = og.totalOrders;

    //public Double [] deliveryTimes = new Double[totalOrders];

    public Order getOrderOnDrone(int i){
        return loadedOrdersList.get(i);
    }

    public double getWeightCapacity(){
        return weightCapacity;
    }

    public Waypoint getCurrentPosition(){
        return currentPosition;
    }

    public Waypoint getCurrentTargetPosition(){
        return targetPositions.get(0);
    }

    public int getTurnAroundTime(){
        return turnAroundTime;
    }

    public void setTurnAroundTime(int t){
        turnAroundTime = t;
    }

}
