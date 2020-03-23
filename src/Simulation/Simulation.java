package Simulation;

import Food.Meal;
import Food.Order;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulation {
    ArrayList<Order> orderQueue = new ArrayList<Order>();
    ArrayList<Meal> mealList = new ArrayList<Meal>();
    PriorityQueue<Order> currentOrderQueue = new PriorityQueue<Order>();

    //Creation of drone for testing purposes at this point
    Drone drone = new Drone();

    private void startSimulation(){
        //should add default meals (for now) to meal list
        //is what calls the run FIFO and runKnapsack methods a specified amount of times
        //calls generateOrderQueue and copy orderQueue when appropriate
    }

    private void generateOrderQueue(){
        //generates a new order queue at the beginning of each pair of simulations
    }

    private void copyOrderQueue(){
        //makes a deep copy of the orderQueue and makes in into a priority queue
    }

    private void runFIFO(){
        //runs FIFO simulations
    }

    private void runKnapsack(){
        //runs knapsack simulations
    }

    //Average and worst delivery time variable, found by getting the average and worst from deliveryTimes
    public double average = getAverage(drone.deliveryTimes);
    public double worst = getWorst(drone.deliveryTimes);

    public static double getAverage(double [] times){
        double sum = 0;
        for(int i = 0; i<times.length; i++){
            sum += times[i];
        }
        return sum/times.length;
    }
    public static double getWorst(double [] times){
        double worst = 0;
        for(int i = 0; i<times.length; i++){
            if(times[i]>worst){
                worst = times[i];
            }
        }
        return worst;
    }
}
