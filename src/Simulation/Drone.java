package Simulation;

import Food.Order;
import Mapping.Waypoint;

import java.util.ArrayList;

public class Drone {
    private ArrayList<Order> loadedOrdersList;
    private double weightCapacity, currentWeight;
    private Waypoint currentPosition;
    private ArrayList<Waypoint> targetPositions;
    private int turnAroundTime;
    public ArrayList<Double> deliveryTimes;

    public Drone(){ //for a default drone
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        //waypoints at start need set
        targetPositions = new ArrayList<Waypoint>();
        turnAroundTime = 3;
        deliveryTimes = new ArrayList<Double>();
    }

    public Drone(Waypoint s){ //for a default drone with a given starting point
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        currentPosition = s;
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

    public void addOrderToDrone(Order o){
        loadedOrdersList.add(o);
        currentWeight += o.getMeal().getTotalWeight();
    }

    public void removeOrderFromDrone(int i){
        currentWeight -= loadedOrdersList.get(i).getMeal().getTotalWeight();
        loadedOrdersList.remove(i);
    }

    public int getNumOrders(){
        return loadedOrdersList.size();
    }

    public double getWeightCapacity(){
        return weightCapacity;
    }

    public double getCurrentWeight(){
        return currentWeight;
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
