package Simulation;

import Food.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulation {
    private ArrayList<Order> orderQueue = new ArrayList<Order>();
    private ArrayList<Meal> mealList = new ArrayList<Meal>(); //stores all different possible meals. Will be passed on constructor
    private PriorityQueue<Order> currentOrderQueue = new PriorityQueue<Order>();
    private int numShifts, timesToBeRan; //passed in constructor, stores number of hours to do the simulation and number of dif sims
    private int[] avgOrders = new int[numShifts];

    //Create array for delivery times with the sum of the number of orders in all of the shifts.
    public float [] deliveryTimes = new float[60];

    //Average and worst delivery time variable, found by getting the average and worst from deliveryTimes
    public float average = Results.getAverage(deliveryTimes);
    public float worst = Results.getWorst(deliveryTimes);

    //the check for adding the probabilities needs to be somewhere. Should be outside of this class
    public Simulation(){
        numShifts = 4;
        timesToBeRan = 1;
        avgOrders[0] = 15;
        avgOrders[1] = 17;
        avgOrders[2] = 22;
        avgOrders[3] = 15;

        ArrayList<Food> temp = new ArrayList<Food>();
        temp.add(new Hamburger());
        temp.add(new Drink());
        temp.add(new Fries());

        mealList.add(new Meal(temp, 0.55));

        temp.add(new Hamburger());
        mealList.add(new Meal(temp, 0.1));

        temp.remove(2);
        temp.remove(3);
        mealList.add(new Meal(temp, 0.2));

        temp.add(new Hamburger());
        mealList.add(new Meal(temp, 0.15));
    }

    public Simulation(int numberOfShifts, int timesToRun, ArrayList<Meal> listOfMeals, int[] avgOrdersEachHour){
        //this will be used when we start having settings for the simulation
    }

    private void runSimulation(){
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

    private void getNextRoute(){
        //will use the drones current location and current orders to solve the traveling salesman problem
    }
}
