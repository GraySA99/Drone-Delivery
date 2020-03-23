package Simulation;

import Food.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulation {
    private ArrayList<Order> orderQueue = new ArrayList<Order>();
    private ArrayList<Meal> mealList = new ArrayList<Meal>(); //stores all different possible meals. Will be passed on constructor
    private ArrayList<Order> currentOrderQueue = new ArrayList<Order>();
    private int numShifts, timesToBeRan; //passed in constructor, stores number of hours to do the simulation and number of dif sims
    private int[] ordersPerHour, times;

    //Creation of drone for testing purposes at this point
    Drone drone = new Drone();

    //the check for adding the probabilities needs to be somewhere. Should be outside of this class
    public Simulation(){
        numShifts = 4;
        ordersPerHour = new int[numShifts];
        timesToBeRan = 1;
        ordersPerHour[0] = 15;
        ordersPerHour[1] = 17;
        ordersPerHour[2] = 22;
        ordersPerHour[3] = 15;

        times = new int[ordersPerHour[0] + ordersPerHour[1] + ordersPerHour[2] + ordersPerHour[3]];

        ArrayList<Food> temp = new ArrayList<Food>();
        temp.add(new Hamburger());
        temp.add(new Drink());
        temp.add(new Fries());

        mealList.add(new Meal(temp, 0.55));

        temp.add(new Hamburger());
        mealList.add(new Meal(temp, 0.1));

        temp.remove(2);
        temp.remove(2);
        mealList.add(new Meal(temp, 0.2));

        temp.add(new Hamburger());
        mealList.add(new Meal(temp, 0.15));
    }

    public Simulation(int numberOfShifts, int timesToRun, ArrayList<Meal> listOfMeals, int[] avgOrdersEachHour){
        //this will be used when we start having settings for the simulation
    }

    public void runSimulation(){
        //should add default meals (for now) to meal list
        //is what calls the run FIFO and runKnapsack methods a specified amount of times
        //calls generateOrderQueue and copy orderQueue when appropriate

        for(int i = 0; i < timesToBeRan; i++){
            generateOrderQueue();
            //copyOrderQueue();

            //runFIFO();

            //copyOrderQueue();

            //runKnapsack();
        }
    }

    private void generateOrderQueue(){
        int count = 0;

        for(int r = orderQueue.size(); r > 0; r--){
            orderQueue.remove(r - 1);
        }

        for(int i = 0; i < numShifts; i++){
            for(int j = 0; j < ordersPerHour[i]; j++){
                //orderQueue.add(new Order(mealList.get((int)(Math.random() * ((mealList.size() - 1) + 1))), )); //need a way to get a list of destinations
                times[count] = (i * 60) + (int) (1 + Math.random() * ((60 - 1) + 1));
                count++;
            }
        }

        heapSort(times);
    }

    private void copyOrderQueue(){
        //makes a deep copy of the orderQueue and makes in into a priority queue
    }

    private void runFIFO(){
        //runs FIFO simulations
        //at start, wait for 5 min or until full
        //go minute by minute. Update the current order queue with any new orders, see where the drone is. if its at the sac pick up orders.
        //if not then let it keep doing its thing. Maybe store the time for the drones current route
        int currentTime = 0;

        while (currentTime < numShifts * 60){
            //fifo
        }
    }

    private void runKnapsack(){
        //runs knapsack simulations
        //at start, wait for 5 min or until full
        //go minute by minute. Update the current order queue with any new orders, see where the drone is. if its at the sac pick up orders.
        //if not then let it keep doing its thing
        int currentTime = 0;

        while (currentTime < numShifts * 60){
            //fifo
        }
    }

    private void getNextRoute(){
        //will use the drones current location and current orders to solve the traveling salesman problem
    }

    public void heapSort(int a[])
    {
        int size = a.length;

        for (int j = size/2 - 1; j >= 0; j--) {
            createHeap(a, j, size);
        }
        int temp;

        for (int i = size - 1; i >= 0; i--) {
            temp = a[0];
            a[0] = a[i];
            a[i] = temp;

            createHeap(a, 0, i);
        }

    }

    void createHeap(int a[], int root, int size)
    {
        int largest = root, left = 2 * root + 1, right = 2 * root + 2;

        if (left < size && a[left] > a[largest]){
            largest = left;
        }
        if (right < size && a[right] > a[largest]){
            largest = right;
        }
        if (largest != root){
            int temp = a[root];
            a[root] = a[largest];
            a[largest] = temp;
            createHeap(a, largest, size);
        }
    }

    //Average and worst delivery time variable, found by getting the average and worst from deliveryTimes
    public Double average = getAverage(drone.deliveryTimes);
    public Double worst = getWorst(drone.deliveryTimes);

    public static Double getAverage(Double [] times){
        double sum = 0;
        for(int i = 0; i<times.length; i++){
            sum += times[i];
        }
        return sum/times.length;
    }
    public static Double getWorst(Double [] times){
        double worst = 0;
        for(int i = 0; i<times.length; i++){
            if(times[i]>worst){
                worst = times[i];
            }
        }
        return worst;
    }
}
