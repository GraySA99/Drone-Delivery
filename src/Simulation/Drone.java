package Simulation;

import Food.Order;
import Mapping.Waypoint;

import java.util.ArrayList;

public class Drone {
    private ArrayList<Order> loadedOrdersList;
    private double weightCapacity, currentWeight;
    private Waypoint currentPosition;
    //private ArrayList<Waypoint> targetPositions; //may not be needed if we just rearrange the list of orders
    private int turnAroundTime, speed, maxFlightTime;
    public ArrayList<ArrayList<Double>> FIFODeliveryTimes, KnapsackDeliveryTimes; //might make a fifo and knapsack list

    //public ArrayList<ArrayList<Double>> TheAbomination = new ArrayList<ArrayList<Double>>();
    //TheAbomination.add(list); //adds list as a list in TheAbomination
    //TheAbomination.get(0).add(i) //adds i as an element of list in TheAbomination
    //TheAbomination.get(0).get(0); //gets the first element of list in TheAbomination

    public Drone(){ //for a default drone
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        currentPosition = null;
        //targetPositions = new ArrayList<Waypoint>();
        turnAroundTime = 3;
        FIFODeliveryTimes = new ArrayList<ArrayList<Double>>();
        KnapsackDeliveryTimes = new ArrayList<ArrayList<Double>>();
        speed = 20;
        maxFlightTime = 20;
    }

    public Drone(Waypoint s){ //for a default drone with a given starting point
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        currentPosition = s;
        //targetPositions = new ArrayList<Waypoint>();
        turnAroundTime = 3;
        FIFODeliveryTimes = new ArrayList<ArrayList<Double>>();
        KnapsackDeliveryTimes = new ArrayList<ArrayList<Double>>();
        speed = 20;
        maxFlightTime = 20;
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

    /*public void setTurnAroundTime(int t){
        turnAroundTime = t;
    }*/

    public void setCurrentPosition(Waypoint w){
        currentPosition = w;
    }

    public int getSpeed(){
        return speed;
    }

    public void addFIFODeliveryTime(int i, double b){
        FIFODeliveryTimes.get(i).add(b);
    }

    public double getFIFODeliveryTime(int i, int j){
        return FIFODeliveryTimes.get(i).get(j);
    }

    public double getKnapsackDeliveryTime(int i, int j){
        return KnapsackDeliveryTimes.get(i).get(j);
    }

    public int getNumFIFODeliveryTimes(){
        return FIFODeliveryTimes.size();
    }

    public int getNumFIFODeliveryTimes(int i){
        return FIFODeliveryTimes.get(i).size();
    }

    public int getNumKnapsackDeliveryTimes(){
        return KnapsackDeliveryTimes.size();
    }

    public int getNumKnapsackDeliveryTimes(int i){
        return KnapsackDeliveryTimes.get(i).size();
    }

    public void addKnapsackDeliveryTime(int i, double b){
        KnapsackDeliveryTimes.get(i).add(b);
    }

    public ArrayList<ArrayList<Double>> getFIFODeliveryTimesList(){
        return FIFODeliveryTimes;
    }

    public ArrayList<ArrayList<Double>> getKnapsackDeliveryTimesList(){
        return KnapsackDeliveryTimes;
    }

    public int getMaxFlightTime(){
        return maxFlightTime;
    }

}
