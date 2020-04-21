package Simulation;

import Food.Food;
import Food.Meal;
import Food.Order;
import Mapping.Map;
import Mapping.Waypoint;

import java.util.ArrayList;

/**
 * Authors: Patrick Reagan and Josh Worley
 * Purpose: A class used for running Drone Delivery Simulations.
 */

public class Simulation {
    private ArrayList<Order> orderQueue; //stores the order queue for a pair of simulations
    private ArrayList<Meal> mealList; //stores all different possible meals. Passed through DataTransfer
    private ArrayList<Order> currentOrderQueue; //a deep copy of the order queue manipulated in a simulation
    private int numShifts, timesToBeRan; //passed from DataTransfer, stores number of shifts in one simulation and number of different simulations
    private int[] ordersPerHour; //stores the average number of orders for each hour in a simulation
    private ArrayList<Integer> times; //stores the times orders will appear
    private Map simMap; //stores the map used by the simulation. Waypoints imported from DataTransfer and map created in constructor
    public Drone drone; //the drone used throughout all of a simulations in a specified number of iterations

    //Public variables for average time and worst time in the simulation, calculated at results
    public Double FIFOaverageTime;
    public Double KSaverageTime;
    public Double FIFOworstTime;
    public Double KSworstTime;

    /**
     * Author: Patrick Reagan
     * Initializes many values used by the simulation. Creates a map from what is available in data transfer. Other default settings are
     * still hardcoded, but some are now pulled from DataTransfer.
     */
    public Simulation(){
        //creation of the simMap
        Waypoint starting = DataTransfer.getWaypoint(0);
        assert DataTransfer.getWaypoints() != null;
        simMap = new Map(starting, DataTransfer.getWaypoints());
        simMap.removeWaypoint(0);
        simMap.getStartingPoint().setStartingPoint(true);

        drone = new Drone();
        orderQueue = new ArrayList<Order>();
        mealList = new ArrayList<Meal>();
        currentOrderQueue = new ArrayList<Order>();

        numShifts = DataTransfer.getNumShifts();
        ordersPerHour = new int[numShifts];
        timesToBeRan = 1;
        ordersPerHour[0] = 38;
        ordersPerHour[1] = 45;
        ordersPerHour[2] = 60;
        ordersPerHour[3] = 30;

        times = new ArrayList<Integer>();

        //creation of the default meals hardcoded for this sprint
        ArrayList<Food> temp = new ArrayList<Food>();
        temp.add(new Food("Hamburger", 0.375));
        temp.add(new Food("Drink", 0.875));
        temp.add(new Food("Fries", 0.25));

        mealList.add(new Meal("One of Each", temp, 0.5));

        temp.add(new Food("Hamburger", 0.375));

        mealList.add(new Meal("Two burgers, one drink, one fry", temp, 0.2));

        temp.remove(1);

        mealList.add(new Meal("Two burgers and one fry", temp, 0.1));

        temp.remove(0);

        mealList.add(new Meal("Burger and a fry", temp, 0.15));

        temp.remove(1);

        mealList.add(new Meal("One Fry", temp, 0.05));
    }

    /**
     * Author: Patrick Reagan
     * Handles running the indicated number of simulations. Calls both FIFO and Knapsack methods, as well as the methods for creating and
     * copying the order queue and other things that need to be reset between simulations
     */
    public void runSimulation(){
        for(int i = 0; i < timesToBeRan; i++){
            generateOrderQueue();
            copyOrderQueue();

            //initializes the number of needed lists for this simulation iteration
            for(int j = 0; j < numShifts; j++){
                drone.getFIFODeliveryTimesList().add(new ArrayList<Double>());
                drone.getKnapsackDeliveryTimesList().add(new ArrayList<Double>());
            }

            drone.setCurrentPosition(simMap.getStartingPoint());
            runFIFO(i);

            copyOrderQueue();

            drone.setCurrentPosition(simMap.getStartingPoint());
            runKnapsack(i);
        }

        //used for debugging to view delivery times
        /*for(int i = 0; i < drone.getNumFIFODeliveryTimes(); i++){
            for(int j = 0; j < drone.getNumFIFODeliveryTimes(i); j++){
                System.out.print(drone.getFIFODeliveryTime(i, j) + " ");
            }
            System.out.print("\n");
        }
        System.out.println("\n");
        for(int i = 0; i < drone.getNumKnapsackDeliveryTimes(); i++){
            for(int j = 0; j < drone.getNumKnapsackDeliveryTimes(i); j++){
                System.out.print(drone.getKnapsackDeliveryTime(i, j) + " ");
            }
            System.out.print("\n");
        }*/
    }

    /**
     * Author: Patrick Reagan
     * Generates a list of random orders and puts them in the simulations order queue. Uses the probabilities for each order to appear and
     * randomly selects a point on the map to generate the specified number of orders in each hour using the meals available in the meals
     * list. Also resets, creates, and populates the times list for when each order appears in the queue.
     */
    private void generateOrderQueue(){
        int curPos, mapPos;
        double prob, curProb;
        boolean found;

        //clears out the previous order queue
        for(int r = orderQueue.size(); r > 0; r--){
            orderQueue.remove(r - 1);
        }

        //clear out times list
        for(int r = times.size(); r > 0; r--){
            times.remove(r - 1);
        }

        for(int i = 0; i < numShifts; i++){
            for(int j = 0; j < ordersPerHour[i]; j++){
                curProb = 0;
                curPos = 0;
                found = false;

                //generates orders according to their given probabilities
                prob = (Math.random()*(1));
                while(curPos < mealList.size() && !found){
                    if(prob < mealList.get(curPos).getProbability() + curProb){
                        found = true;
                    } else {
                        curProb += mealList.get(curPos).getProbability();
                        curPos++;
                    }
                }
                //finds a random delivery location to give an order based off of the current map
                mapPos = ((int) (Math.random()*(simMap.getSize())));
                orderQueue.add(new Order(mealList.get(curPos), simMap.getMapPoint(mapPos)));
                //adds a new time for each order, however for the moment, this isn't the time that corresponds with this order
                times.add((i * 60) + (int) (1 + Math.random() * ((60 - 1) + 1)));
            }
        }

        //sorts the times array from least to greatest
        heapSort(times);
        //the times now correspond with the orders in the orderQueue

        //used for debugging to look at the available orders
        /*for(int r = 0; r < orderQueue.size(); r++){
            System.out.println("At time " + times.get(r) + " An order with " + orderQueue.get(r).getMeal().getName() + " to " + orderQueue.get(r).getDestination().getName() + " will appear in the queue.");
        }*/
    }

    /**
     * Author: Patrick Reagan
     * Creates a deep copy of the orderQueue and stores it in currentOrderQueue to allow modifications during a simulation
     */
    private void copyOrderQueue(){
        for(int i = 0; i < orderQueue.size(); i++){
            currentOrderQueue.add(orderQueue.get(i));
        }
    }

    /**
     * Author: Patrick Reagan
     * Handles running a single FIFO simulation.
     * @param iteration what number iteration it currently is.
     */
    private void runFIFO(int iteration){
        double currentTime = 0, tripTime = 0, calcTime, homeTime; //currentTime is the current time in the simultion, tripTime tracks how
        //long a drone has been running since it was at the start point, calcTime and homeTime are used for storing various calculation results
        int currentOrder = 0; //tracks the current order in currentOrderQueue
        boolean launched, canLoad; //both are used when the drone is loading to stop it from loading when needed

        while ((currentOrderQueue.size() > 0 || drone.getNumOrders() > 0)){
            //when the drone is at the starting point
            if(drone.getCurrentPosition().isStarting()){
                launched = false;
                canLoad = true;

                if(tripTime > 0){
                    currentTime += drone.getTurnAroundTime();
                    tripTime = 0;
                }

                //when the drone is at the starting point and it's within the first five minutes of the simulation
                if(currentTime <= 5){
                    //the drone will add any available orders if it can. If the drone is full then it will launch
                    while(!launched && currentTime <= 5){
                        //this loop goes through all the available order, starting with the first, and tries to add them if possible
                        while(times.get(currentOrder) <= currentTime && !launched && currentTime <= 5){
                            if(drone.getCurrentWeight() + currentOrderQueue.get(0).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                loadOrder(currentOrder);
                                currentOrder++;
                            } else { //launch
                                //orders on the drone are sorted and the first order is delivered
                                drone.setOrdersList(sortOrders(drone.getOrdersList()));
                                calcTime = calculateTime(simMap.getStartingPoint(), drone.getOrderOnDrone(0).getDestination());
                                currentTime += calcTime;
                                tripTime += calcTime;
                                deliverOrderFIFO(currentTime, iteration);
                                launched = true;
                            }
                            if(currentTime == 5){
                                launched = true;
                            }
                        }

                        if(times.get(currentOrder) > currentTime && !launched){//increment time if not full
                            currentTime++;
                        }
                    }

                    //when the drone is at the starting position and it's not within the first five minutes of the simulation
                } else {
                    //resets the pickup times for the orders that were loaded before the five minute mark
                    //this launches the drone at minute 6 if there is anything on it so that it doesn't try and wait around any more
                    if(currentTime < 7){
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        }
                    }
                    //loading phase
                    while(canLoad && currentOrder <= times.size() - 1){
                        //this loop goes through all the available orders, starting with the first, and tries to add them if possible
                        while(times.get(currentOrder) <= currentTime && canLoad){//loads available orders until weight capacity is hit
                            if(drone.getCurrentWeight() + currentOrderQueue.get(0).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                loadOrder(currentOrder);
                                currentOrder++;
                                if(currentOrder >= times.size() - 1){
                                    canLoad = false;
                                    break;
                                }
                            } else {
                                canLoad = false;
                            }
                        }

                        //the drone will launch if it has anything on it
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        } else if(times.get(currentOrder) > currentTime && canLoad){ //the drone will wait for more orders
                            currentTime++;
                        }
                    }

                    //orders on the drone are sorted and the first order is delivered
                    drone.setOrdersList(sortOrders(drone.getOrdersList()));
                    calcTime = calculateTime(simMap.getStartingPoint(), drone.getOrderOnDrone(0).getDestination());
                    currentTime += calcTime;
                    tripTime += calcTime;
                    deliverOrderFIFO(currentTime, iteration);
                }
            } else { //the drone is not at the starting point
                //the drone is not at the starting point and is empty
                if(drone.getNumOrders() == 0){
                    calcTime = calculateTime(drone.getCurrentPosition(), simMap.getStartingPoint());
                    currentTime += calcTime;
                    tripTime += calcTime;
                    drone.setCurrentPosition(simMap.getStartingPoint());
                } else { //the drone is not home and has orders
                    //the drone figures out if it can make it to its next destination and home if it needs to charge
                    calcTime = calculateTime(drone.getCurrentPosition(), drone.getOrderOnDrone(0).getDestination());
                    homeTime = calculateTime(drone.getOrderOnDrone(0).getDestination(), simMap.getStartingPoint());

                    //the drone returns home if it cannot make it to the next stop and home
                    if(tripTime + calcTime + homeTime > drone.getMaxFlightTime() * 0.95){
                        calcTime = calculateTime(drone.getCurrentPosition(), simMap.getStartingPoint());
                        currentTime += calcTime;
                        drone.setCurrentPosition(simMap.getStartingPoint());
                        tripTime = 0;
                    } else { //the drone continues its current delivery path
                        currentTime += calcTime;
                        tripTime += calcTime;
                        deliverOrderFIFO(currentTime, iteration);
                    }
                }
            }
        }
    }

    /**
     * Author: Patrick Reagan
     * Handles running a single Knapsack simulation.
     * @param iteration what number iteration it currently is.
     */
    private void runKnapsack(int iteration){
        double currentTime = 0, tripTime = 0, calcTime, homeTime; //currentTime is the current time in the simultion, tripTime tracks how
        //long a drone has been running since it was at the start point, calcTime and homeTime are used for storing various calculation results
        int currentOrder = 0; //tracks the current order in currentOrderQueue
        boolean launched, canLoad, skip = false; //both launched and canLoad are used when the drone is loading to stop it from loading
        // when needed, skip is used when an order is skipped within the currentOrderQueue and added to the skipped queue
        ArrayList<Order> skippedList = new ArrayList<Order>(); //keeps a list of skipped orders
        ArrayList<Integer> skipped = new ArrayList<Integer>(); //stores the number of times skipped orders have been skipped in knapsack

        while ((currentOrderQueue.size() > 0 || drone.getNumOrders() > 0 || skippedList.size() > 0)){
            //when the drone is at the starting point
            if(drone.getCurrentPosition().isStarting()){
                launched = false;
                canLoad = true;

                if(tripTime > 0){
                    currentTime += drone.getTurnAroundTime();
                    tripTime = 0;
                }

                //when the drone is at the starting point and it's within the first five minutes of the simulation
                if(currentTime <= 5){
                    //the drone will add any available orders if it can. If the drone is full then it will launch
                    while(!launched && currentTime <= 5){
                        //this loop goes through all the available order, starting with the first, and tries to add them if possible
                        while(times.get(currentOrder) <= currentTime && !launched && currentTime <= 5){
                            if(drone.getCurrentWeight() + currentOrderQueue.get(0).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                //System.out.print("Added order to drone that was available at time " + times.get(currentOrder) + " at time " + currentTime + "\t");
                                loadOrder(currentOrder);
                                currentOrder++;
                            } else { //launch
                                //orders on the drone are sorted and the first order is delivered
                                drone.setOrdersList(sortOrders(drone.getOrdersList()));
                                calcTime = calculateTime(simMap.getStartingPoint(), drone.getOrderOnDrone(0).getDestination());
                                currentTime += calcTime;
                                tripTime += calcTime;
                                deliverOrderKnapsack(currentTime, iteration);
                                launched = true;
                            }
                            if(currentTime == 5){
                                launched = true;
                            }
                        }

                        if(times.get(currentOrder) > currentTime && !launched){//increment time if not full
                            currentTime++;
                        }
                    }

                    //when the drone is at the starting position and it's not within the first five minutes of the simulation
                } else {
                    //resets the pickup times for the orders that were loaded before the five minute mark
                    //this launches the drone at minute 6 if there is anything on it so that it doesn't try and wait around any more
                    if(currentTime < 7){
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        }
                    }
                    while(canLoad && currentOrder <= times.size() - 1){
                        int max = 0, min;
                        int start = 0;
                        boolean hasSkipped = false; //tracks if an order has been skipped for the current loading phase

                        //the previous first order was skipped, it will be added automatically
                        while(skip && skippedList.size() > 0 && start < skippedList.size()){
                            //adds the minimum weight orders to the drone starting at the most skipped ones, and working its way down
                            min = start;
                            for(int r = start; r < skippedList.size(); r++){
                                if(skippedList.get(min).getMeal().getTotalWeight() > skippedList.get(r).getMeal().getTotalWeight() && skipped.get(r).equals(skipped.get(start))){
                                    min = r;
                                }
                            }

                            //adds the minimum for the current number of skips to the drone if it can
                            if(drone.getCurrentWeight() + skippedList.get(min).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                drone.addOrderToDrone(skippedList.get(min));
                                skippedList.remove(min);
                                if(currentOrder >= times.size() - 1){
                                    canLoad = false;
                                    break;
                                }
                            } else {
                                start++;
                            }

                            //the skipped list is empty so we don't need to check for skipped orders
                            if(skippedList.size() == 0){
                                skip = false;
                            }
                        }

                        //adds a skipped counter to the orders still remaining in the skipped queue
                        for(int c = 0; c < skipped.size(); c++){
                            skipped.set(c, (skipped.get(c) + 1));
                        }

                        //loading phase
                        while(times.get(currentOrder) <= currentTime && canLoad){//loads available orders until weight capacity is hit
                            //loop to find if the first order is the greatest
                            if(!hasSkipped && currentOrderQueue.size() > 1){
                                for(int r = 0; r < times.size() - currentOrder; r++){
                                    if(times.get(currentOrder + r) <= currentTime &&  currentOrderQueue.get(max).getMeal().getTotalWeight() < currentOrderQueue.get(r).getMeal().getTotalWeight()){
                                        max = r;
                                    }

                                }
                            }

                            //adds the largest order to the skipped queue
                            if(currentOrderQueue.size() > 1){
                                if(times.get(currentOrder + 1) <= currentTime && drone.getCurrentWeight() + currentOrderQueue.get(1).getMeal().getTotalWeight() < drone.getWeightCapacity()){
                                    if(max == 0){
                                        skip = true;
                                        hasSkipped = true;
                                        skippedList.add(currentOrderQueue.get(0));
                                        currentOrderQueue.remove(0);
                                        skipped.add(1);
                                        currentOrder++;
                                    }
                                }
                            }

                            //adds the next first in the queue if it is available as we know it's not the largest
                            if(times.get(currentOrder) <= currentTime && drone.getCurrentWeight() + currentOrderQueue.get(0).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                loadOrder(currentOrder);
                                currentOrder++;
                                if(currentOrder >= times.size() - 1){
                                    canLoad = false;
                                    break;
                                }
                            }
                        }

                        //adds any orders that were skipped that may fit into the drone before going to the next minute
                        while(skip && skippedList.size() > 0 && start < skippedList.size()){
                            //adds the minimum weight orders to the drone starting at the most skipped ones, and working its way down
                            min = start;
                            for(int r = start; r < skippedList.size(); r++){
                                if(skippedList.get(min).getMeal().getTotalWeight() > skippedList.get(r).getMeal().getTotalWeight() && skipped.get(r).equals(skipped.get(start))){
                                    min = r;
                                }
                            }

                            //adds the minimum for the current number of skips to the drone if it can
                            if(drone.getCurrentWeight() + skippedList.get(min).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                drone.addOrderToDrone(skippedList.get(min));
                                skippedList.remove(min);
                                if(currentOrder >= times.size() - 1){
                                    canLoad = false;
                                    break;
                                }
                            } else {
                                start++;
                            }

                            //the skipped list is empty so we don't need to check for skipped orders
                            if(skippedList.size() == 0){
                                skip = false;
                            }
                        }

                        //the drone will launch if it has anything on it
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        } else if(times.get(currentOrder) > currentTime && canLoad){ //the drone will wait for more orders
                            currentTime++;
                        }
                    }

                    drone.setOrdersList(sortOrders(drone.getOrdersList()));
                    calcTime = calculateTime(simMap.getStartingPoint(), drone.getOrderOnDrone(0).getDestination());
                    currentTime += calcTime;
                    tripTime += calcTime;
                    deliverOrderKnapsack(currentTime, iteration);
                }
            } else { //the drone is not at the starting point
                //the drone is not home and is empty, so it goes back to the start point
                if(drone.getNumOrders() == 0){
                    calcTime = calculateTime(drone.getCurrentPosition(), simMap.getStartingPoint());
                    currentTime += calcTime;
                    tripTime += calcTime;
                    drone.setCurrentPosition(simMap.getStartingPoint());
                } else { //the drone is not home and has orders
                    //the drone figures out if it can make it to its next destination and home if it needs to charge
                    calcTime = calculateTime(drone.getCurrentPosition(), drone.getOrderOnDrone(0).getDestination());
                    homeTime = calculateTime(drone.getOrderOnDrone(0).getDestination(), simMap.getStartingPoint());

                    //the drone returns home if it cannot make it to the next stop and home
                    if(tripTime + calcTime + homeTime > drone.getMaxFlightTime()  * 0.95){
                        calcTime = calculateTime(drone.getCurrentPosition(), simMap.getStartingPoint());
                        currentTime += calcTime;
                        drone.setCurrentPosition(simMap.getStartingPoint());
                        tripTime = 0;
                    } else { //the drone continues its current delivery path
                        currentTime += calcTime;
                        tripTime += calcTime;
                        deliverOrderKnapsack(currentTime, iteration);
                    }
                }
            }
        }
    }

    //||-----------Utility Methods Between FIFO and Knapsack-----------||

    /**
     * Author: Patrick Reagan
     * Loads the first order onto the drone.
     * @param currentOrder is used to add the correct pickup time to the loaded order.
     */
    private void loadOrder(int currentOrder){
        currentOrderQueue.get(0).setPickUpTime(times.get(currentOrder));
        drone.addOrderToDrone(currentOrderQueue.get(0));
        currentOrderQueue.remove(0);
    }

    /**
     * Author: Patrick Reagan
     * Calculates the time to travel between two Waypoints.
     * @param a is the first waypoint.
     * @param b is the second waypoint.
     * @return the time for the drone to travel between the two Waypoints in minutes.
     */
    private double calculateTime(Waypoint a, Waypoint b){
        return Math.round((((distance(a, b) * 1) / (drone.getSpeed() * 1609.34)) / 60 + .5) * 100.00) / 100.00;
    }

    /**
     * Author: Patrick Reagan
     * Delivers an order in the FIFO simulation
     * @param currentTime is the time of delivery and is used to find the delivery time for the order
     * @param iteration is used for finding which hour the order was available in
     */
    private void deliverOrderFIFO(double currentTime, int iteration){
        //finds what hour the orders pickup time is from
        int a = 1;
        while(drone.getOrderOnDrone(0).getPickUpTime() - (60 * a) > 0){
            a++;
        }
        drone.addFIFODeliveryTime((a - 1) + (numShifts * iteration),currentTime - drone.getOrderOnDrone(0).getPickUpTime());
        drone.setCurrentPosition(drone.getOrderOnDrone(0).getDestination());
        drone.removeOrderFromDrone(0);
    }

    /**
     * Author: Patrick Reagan
     * Delivers an order in the Knapsack simulation
     * @param currentTime is the time of delivery and is used to find the delivery time for the order
     * @param iteration is used for finding which hour the order was available in
     */
    private void deliverOrderKnapsack(double currentTime, int iteration){
        //finds what hour the orders pickup time is from
        int a = 1;
        while(drone.getOrderOnDrone(0).getPickUpTime() - (60 * a) > 0){
            a++;
        }
        drone.addKnapsackDeliveryTime((a - 1) + (numShifts * iteration), currentTime - drone.getOrderOnDrone(0).getPickUpTime());
        drone.setCurrentPosition(drone.getOrderOnDrone(0).getDestination());
        drone.removeOrderFromDrone(0);
    }

    //The TSP Route Calculator - Josh
    //Searches the Routes Search tree recursively with DFS
    //and uses backtracking to efficiently find best route
    private int[] tspRoute(double[][] graph, int[] solution, int[] bestSolution, boolean[] visited,
                          int currentNode, int level, int numNodes) {
        boolean debug = false;

        if(debug)
            System.out.println("I'm at Node: " + currentNode);

        //modify solution SO FAR
        solution[level] = currentNode;

        //calculate the COST SO FAR
        double costSoFar = 0.0;
        int lastNode = 0;
        for(int i = 1; i <= level; i++) {
            int node1 = solution[i - 1];
            int node2 = solution[i];
            costSoFar += graph[node1][node2];
            if(i == level)
                lastNode = solution[i];
        }

        //find BEST COST SO FAR
        double bestCostSoFar = 0.0;
        for(int i = 1; i < bestSolution.length; i++) {
            int node1 = bestSolution[i - 1];
            int node2 = bestSolution[i];
            bestCostSoFar += graph[node1][node2];
        }


        //BACKTRACK if I've passed the best cost so far
        //AND we're not just at the beginning node
        //AND we actually have a bestCostSoFar
        //(Agent will go down left side of tree and find the first complete cost
        // and that cost will be set as the first bestCostSoFar)
        if(debug)
            System.out.println("costSoFar: " + costSoFar + " bestCostSoFar: " + bestCostSoFar);
        if(costSoFar > bestCostSoFar && level != 0 && bestCostSoFar != 0.0) {
            if(debug)
                System.out.println("RETURNING");
            return solution;
        }


        //BOTTOM OF RECURSIVE CALL
        //I've hit the second to last level of tree, still need to check path back to 1
        if(level == numNodes - 1) {
            if(debug)
                System.out.println("HIT BOTTOM");

            //add going to 0 to my current solution
            solution[solution.length - 1] = 0;
            costSoFar += graph[lastNode][0];

            //For testing purposes only
            if(debug ) {
                System.out.print("CURRENT SOLUTION: ");
                for(int i = 0; i < solution.length; i++)
                    System.out.print(solution[i] + " ");
                System.out.println("COST: " + costSoFar);
            }

            // check solution against best solution
            // If my current cost is better than my best cost so far, OR
            // if I don't yet have a best cost so far, make one
            if(costSoFar < bestCostSoFar || bestCostSoFar == 0) {
                if(debug)
                    System.out.println("REASSIGNING BESTSOLUTION");
                for(int i = 0; i < bestSolution.length; i++) {
                    bestSolution[i] = solution[i];
                }
            }

            if(debug) {
                System.out.println("RETURNING TO PARENT...");
                System.out.println("");
            }
            return solution;
        }

        visited[currentNode] = true;
        for(int i = 0; i < numNodes; i++) {
            if(!visited[i]) {
                if(debug)
                    System.out.println("I'm in level " + level + " node " + currentNode + " and visiting node " + i);
                solution = tspRoute(graph, solution, bestSolution, visited,
                        i, level + 1, numNodes);
                if(debug)
                    System.out.println("I'm back in level " + level + " and node " + currentNode);
            }
        }
        visited[currentNode] = false;

        if(level == 0) {
            if(debug)
                System.out.println("FINAL RETURN...");
            return bestSolution;
        }
        else {
            if(debug)
                System.out.println("RETURNING TO PARENT...");
            return solution;
        }
    }

    //This uses the TSP algorithm to find the best possible route
    //returns: an ordered version of the "orders" arraylist passed in
    private ArrayList<Order> sortOrders(ArrayList<Order> orders) {
        // I need to first set the starting point, statically set to the SAC temporarily
        //Waypoint start = new Waypoint("SAC", 41.154870, -80.077945, true);
        Waypoint start = simMap.getStartingPoint();
        orders.add(0, new Order(new Meal(), start));

        int numNodes = orders.size();
        double[][] graph = new double[numNodes][numNodes];

        //Now that I have the points, I need to make the graph
        for(int node = 0; node < orders.size(); node++) {
            for(int otherNode = 0; otherNode < orders.size(); otherNode++) {
                graph[node][otherNode] = distance(orders.get(node).getDestination(), orders.get(otherNode).getDestination());
            }
        }

        int[] solution = new int[numNodes + 1];
        int[] bestSolution = new int[numNodes + 1];
        for(int i = 0; i < numNodes + 1; i++) {
            solution[i] = 0;
            bestSolution[i] = 0;
        }

        boolean[] visited = new boolean[numNodes];
        for(int i = 0; i < numNodes; i++)
            visited[i] = false;

        bestSolution = tspRoute(graph, solution, bestSolution, visited, 0, 0, numNodes);

        //Reorder 'orders', at this point, it's "SAC", "HAL", "Ketler",...
        ArrayList<Order> sortedOrders = new ArrayList<>();

        // We want to start at the first
        for(int i = 1; i < bestSolution.length - 1; i++) {
            //System.out.println("Putting the " + bestSolution[i] + "th index of orders in sortedOrders");
            sortedOrders.add(orders.get(bestSolution[i]));
        }

        return sortedOrders;
    }

    // returns the distance in meters between wp1 and wp2
    public double distance(Waypoint wp1, Waypoint wp2) {
        int earthRadius = 6371000; // distance in meters
        double lat1 = Math.toRadians(wp1.getLatitude());
        double lat2 = Math.toRadians(wp2.getLatitude());
        double dLat = Math.toRadians(wp2.getLatitude() - wp1.getLatitude());
        double dLong = Math.toRadians(wp2.getLongitude() - wp1.getLongitude());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLong/2) * Math.sin(dLong/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

    /**
     * Conducts a heapSort for a given ArrayList.
     * @param a is the ArrayList to be sorted.
     */
    public void heapSort(ArrayList<Integer> a)
    {
        int size = a.size();

        for (int j = size/2 - 1; j >= 0; j--) {
            createHeap(a, j, size);
        }
        int temp;

        for (int i = size - 1; i >= 0; i--) {
            temp = a.get(0);
            a.set(0, a.get(i));
            a.set(i, temp);

            createHeap(a, 0, i);
        }

    }

    /**
     * Used recursively by the heapSort method
     * @param a is the arrayList being sorted.
     * @param root is the current starting root
     * @param size is the size of the list.
     */
    void createHeap(ArrayList<Integer> a, int root, int size)
    {
        int largest = root, left = 2 * root + 1, right = 2 * root + 2;

        if (left < size && a.get(left) > a.get(largest)){
            largest = left;
        }
        if (right < size && a.get(right) > a.get(largest)){
            largest = right;
        }
        if (largest != root){
            int temp = a.get(root);
            a.set(root, a.get(largest));
            a.set(largest, temp);
            createHeap(a, largest, size);
        }
    }
}
