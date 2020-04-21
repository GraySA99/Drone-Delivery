package Simulation;

import Food.Order;
import Mapping.Waypoint;

import java.util.ArrayList;

/**
 * Author: Patrick Reagan
 * Purpose: Used during a simulation as a simulated drone delivering orders to various specified Waypoints
 */

public class Drone {
    private ArrayList<Order> loadedOrdersList; //the list of orders currently on the Drone
    private double weightCapacity, currentWeight, turnAroundTime; //stores the Drones weight capacity, current stored weight, and turn around time
    private Waypoint currentPosition; //stores the Drone's current position
    private int speed, maxFlightTime; //stores the drones speed and max flight time
    //these crazy lists will probably be their own class in Sprint 2
    public ArrayList<ArrayList<Double>> FIFODeliveryTimes, KnapsackDeliveryTimes; //stores the lists of lists of delivery times for each
    // hour of each simulation ran


    /**
     * Creates a Drone with no starting Waypoint and initializes the drone's stored values.
     */
    public Drone(){ //for a default drone
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        currentPosition = null;
        turnAroundTime = 2.5;
        FIFODeliveryTimes = new ArrayList<ArrayList<Double>>();
        KnapsackDeliveryTimes = new ArrayList<ArrayList<Double>>();
        speed = 25;
        maxFlightTime = 20;
    }

    /**
     * Creates a Drone with a starting Waypoint and initializes the drone's stored values..
     * @param s is the Drone's starting Waypoint.
     */
    public Drone(Waypoint s){ //for a default drone with a given starting point
        loadedOrdersList  = new ArrayList<Order>();
        weightCapacity = 12;
        currentWeight = 0;
        currentPosition = s;
        turnAroundTime = 2.5;
        FIFODeliveryTimes = new ArrayList<ArrayList<Double>>();
        KnapsackDeliveryTimes = new ArrayList<ArrayList<Double>>();
        speed = 20;
        maxFlightTime = 25;
    }

    /**
     * Returns a specified order in the Drone's list of carried orders.
     * @param i is the index of the requested order.
     * @return the ith index order on the Drone.
     */
    public Order getOrderOnDrone(int i){
        return loadedOrdersList.get(i);
    }

    /**
     * Returns the Drone's order list.
     * @return the Drone's order list.
     */
    public ArrayList<Order> getOrdersList(){
        return loadedOrdersList;
    }

    /**
     * Clears out the previous orders list and sets it to a new one. Used after the list is given to the TSP method and sorted.
     * @param o is the new list the Drone will be given.
     */
    public void setOrdersList(ArrayList<Order> o){
        //clears out old orders list
        for(int r = loadedOrdersList.size(); r > 0; r--){
            loadedOrdersList.remove(r - 1);
        }

        for(int i = 0; i < o.size(); i++){
            loadedOrdersList.add(o.get(i));
        }
    }

    /**
     * Adds an order to the Drone's list of orders and recalculates the Drone's current weight.
     * @param o is the order being added.
     */
    public void addOrderToDrone(Order o){
        loadedOrdersList.add(o);
        currentWeight += o.getMeal().getTotalWeight();
    }

    /**
     * Removes a specified order from the Drone and recalculates the Drone's current weight.
     * @param i is the index of the order to be removed.
     */
    public void removeOrderFromDrone(int i){
        currentWeight -= loadedOrdersList.get(i).getMeal().getTotalWeight();
        loadedOrdersList.remove(i);
    }

    /**
     * Returns the number of orders stored in the Drone's list of orders.
     * @return the number of orders on the Drone.
     */
    public int getNumOrders(){
        return loadedOrdersList.size();
    }

    /**
     * Returns the Drone's weight capacity.
     * @return the Drone's weight capacity;
     */
    public double getWeightCapacity(){
        return weightCapacity;
    }

    /**
     * Returns the current weight stored on the drone.
     * @return the current weight of the Drone.
     */
    public double getCurrentWeight(){
        return currentWeight;
    }

    /**
     * Returns the Drone's current position.
     * @return the Drone's current position.
     */
    public Waypoint getCurrentPosition(){
        return currentPosition;
    }

    /**
     * Returns the Drone's turn around time.
     * @return the Drone's  turn around time.
     */
    public double getTurnAroundTime(){
        return turnAroundTime;
    }

    /**
     * Sets the Drone's position to a specified Waypoint.
     * @param w is the Waypoint the Drone's position is being set to.
     */
    public void setCurrentPosition(Waypoint w){
        currentPosition = w;
    }

    /**
     * Return the speed of the Drone.
     * @return the speed of the Drone.
     */
    public int getSpeed(){
        return speed;
    }

    /**
     * Adds a FIFO delivery time to a specified hour's list.
     * @param i is the index of the desired hour.
     * @param b is the delivery time being added.
     */
    public void addFIFODeliveryTime(int i, double b){
        FIFODeliveryTimes.get(i).add(b);
    }

    /**
     * Returns a specified FIFO delivery time.
     * @param i is the index of the hour the delivery time is in.
     * @param j is the position in the hour's list of the delivery time.
     * @return the specified delivery time.
     */
    public double getFIFODeliveryTime(int i, int j){
        return FIFODeliveryTimes.get(i).get(j);
    }

    /**
     * Returns a specified Knapsack delivery time.
     * @param i is the index of the hour the delivery time is in.
     * @param j is the position in the hour's list of the delivery time.
     * @return the specified delivery time.
     */
    public double getKnapsackDeliveryTime(int i, int j){
        return KnapsackDeliveryTimes.get(i).get(j);
    }

    /**
     * Returns the number of hours of stored FIFO times.
     * @return the number of hours of stored FIFO times.
     */
    public int getNumFIFODeliveryTimes(){
        return FIFODeliveryTimes.size();
    }

    /**
     * Returns the number of FIFO delivery times stored for a specified hour.
     * @param i the index of the desired delivery time hour.
     * @return the the number of delivery times stored specified hour's delivery's list.
     */
    public int getNumFIFODeliveryTimes(int i){
        return FIFODeliveryTimes.get(i).size();
    }

    /**
     * Returns the number of hours of stored Knapsack times.
     * @return the number of hours of stored Knapsack times.
     */
    public int getNumKnapsackDeliveryTimes(){
        return KnapsackDeliveryTimes.size();
    }

    /**
     * Returns the number of Knapsack delivery times stored for a specified hour.
     * @param i the index of the desired delivery time hour.
     * @return the the number of delivery times stored specified hour's delivery's list.
     */
    public int getNumKnapsackDeliveryTimes(int i){
        return KnapsackDeliveryTimes.get(i).size();
    }

    /**
     * Adds a Knapsack delivery time to a specified hour's list.
     * @param i is the index of the desired hour.
     * @param b is the delivery time being added.
     */
    public void addKnapsackDeliveryTime(int i, double b){
        KnapsackDeliveryTimes.get(i).add(b);
    }

    /**
     * Returns the list containing the lists of each hours FIFO delivery times.
     * @return the list containing the lists of each hours FIFO delivery times.
     */
    public ArrayList<ArrayList<Double>> getFIFODeliveryTimesList(){
        return FIFODeliveryTimes;
    }

    /**
     * Returns the list containing the lists of each hours FIFO delivery times.
     * @return the list containing the lists of each hours FIFO delivery times.
     */
    public ArrayList<ArrayList<Double>> getKnapsackDeliveryTimesList(){
        return KnapsackDeliveryTimes;
    }

    /**
     * Returns the Drone's maximum flight time before needing to return to the start point.
     * @return the Drone's maximum flight time before needing to return to the start point.
     */
    public int getMaxFlightTime(){
        return maxFlightTime;
    }

}
