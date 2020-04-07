package Simulation;

import Food.Order;
import Mapping.Waypoint;

import java.util.ArrayList;

public class Drone {
    private ArrayList<Order> loadedOrdersList;
    private double weightCapacity, currentWeight;
    private Waypoint currentPosition;
    //private ArrayList<Waypoint> targetPositions; //may not be needed if we just rearrange the list of orders
    private int turnAroundTime, speed;
    public ArrayList<Double> deliveryTimes;

    public Drone(){ //for a default drone
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        currentPosition = null;
        //targetPositions = new ArrayList<Waypoint>();
        turnAroundTime = 3;
        deliveryTimes = new ArrayList<Double>();
        speed = 20;
    }

    public Drone(Waypoint s){ //for a default drone with a given starting point
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        currentPosition = s;
        //targetPositions = new ArrayList<Waypoint>();
        turnAroundTime = 3;
        deliveryTimes = new ArrayList<Double>();
        speed = 20;
    }

    //Create array for delivery times with the sum of the number of orders in all of the shifts.
    //OrderGenerator og = new OrderGenerator();
    //int totalOrders = og.totalOrders;

    //public Double [] deliveryTimes = new Double[totalOrders];

    public Order getOrderOnDrone(int i){
        return loadedOrdersList.get(i);
    }

    public ArrayList<Order> getOrdersList(){
        return loadedOrdersList;
    }

    public void setOrdersList(ArrayList<Order> o){
        //clears out old orders list
        for(int r = loadedOrdersList.size(); r > 0; r--){
            loadedOrdersList.remove(r - 1);
        }

        for(int i = 0; i < o.size(); i++){
            loadedOrdersList.add(o.get(i));
        }
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

    /*public Waypoint getCurrentTargetPosition(){
        return targetPositions.get(0);
    }*/

    public int getTurnAroundTime(){
        return turnAroundTime;
    }

    public void setTurnAroundTime(int t){
        turnAroundTime = t;
    }

    public void setCurrentPosition(Waypoint w){
        currentPosition = w;
    }

    public int getSpeed(){
        return speed;
    }

    public void addDeliveryTime(double b){
        deliveryTimes.add(b);
    }

}
