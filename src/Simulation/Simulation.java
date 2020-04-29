package Simulation;

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
    private static final int MINUTES_TO_WAIT_AT_START = 5, MINUTES_PER_SHIFT = 60,
            FIFO_SIM_ID = 1, KNAPSACK_SIM_ID = 2;
    private static final double PERCENTAGE_OF_FLIGHT_TIME_NOT_TO_EXCEED = 0.95;
    private ArrayList<Order> orderQueue; //stores the order queue for a pair of simulations
    private ArrayList<Meal> mealList; //stores all different possible meals. Passed through DataTransfer
    private ArrayList<Order> currentOrderQueue; //a deep copy of the order queue manipulated in a simulation
    private int numShifts, timesToBeRan; //passed from DataTransfer, stores number of shifts in one simulation and number of different simulations
    private int[] ordersPerHour; //stores the average number of orders for each hour in a simulation
    private ArrayList<Integer> times; //stores the times orders will appear
    private Map simMap; //stores the map used by the simulation. Waypoints imported from DataTransfer and map created in constructor
    public Drone drone; //the drone used throughout all of a simulations in a specified number of iterations

    private double currentTime, tripTime;
    private int currentOrder;

    //Public variables for average time and worst time in the simulation, calculated at results
    public Double FIFOaverageTime;
    public Double KSaverageTime;
    public Double FIFOworstTime;
    public Double KSworstTime;

    /**
     * Author: Patrick Reagan
     * Initializes many values used by the simulation. All settings are pulled from DataTransfer.
     */
    public Simulation(){
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
        timesToBeRan = DataTransfer.getNumSimulations();

        for(int ordersPerHourIndex = 0; ordersPerHourIndex < numShifts; ordersPerHourIndex++){
            ordersPerHour[ordersPerHourIndex] = DataTransfer.getShift(ordersPerHourIndex + 1);
        }

        times = new ArrayList<Integer>();

        for(int mealIndex = 0; mealIndex < DataTransfer.getNumMeals(); mealIndex++){
            mealList.add(DataTransfer.getMeal(mealIndex));
        }
    }

    /**
     * Author: Patrick Reagan
     * Handles running the indicated number of simulations. Calls both FIFO and Knapsack methods, as well as the methods for creating and
     * copying the order queue and other things that need to be reset between simulations
     */
    public void runSimulation(){
        for(int currentIteration = 0; currentIteration < timesToBeRan; currentIteration++){
            generateOrderQueue();
            copyOrderQueue();

            //initializes the number of needed lists for this simulation iteration
            for(int addedLists = 0; addedLists < numShifts; addedLists++){
                drone.getFIFODeliveryTimesList().getDeliveryTimesList().add(new ArrayList<Double>());
                drone.getKnapsackDeliveryTimesList().getDeliveryTimesList().add(new ArrayList<Double>());
            }

            setInitialSimValues();
            runFIFO(currentIteration);

            copyOrderQueue();

            setInitialSimValues();
            runKnapsack(currentIteration);
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

    private void setInitialSimValues(){
        currentTime = 0;
        tripTime = 0;
        currentOrder = 0;
        drone.setCurrentPosition(simMap.getStartingPoint());
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

        //clears out the previous order queue to ensure it is reset from previous simulation
        for(int numRemaining = orderQueue.size(); numRemaining > 0; numRemaining--){
            orderQueue.remove(numRemaining - 1);
        }

        //clear out times list to ensure it is reset from previous simulation
        for(int numRemaining = times.size(); numRemaining > 0; numRemaining--){
            times.remove(numRemaining - 1);
        }

        for(int shiftIndex = 0; shiftIndex < numShifts; shiftIndex++){
            for(int currentOrderIndex = 0; currentOrderIndex < ordersPerHour[shiftIndex]; currentOrderIndex++){
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
                times.add((shiftIndex * MINUTES_PER_SHIFT) + (int) (1 + Math.random() * ((MINUTES_PER_SHIFT - 1) + 1)));
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
        double calcTime, homeTime; // calcTime and homeTime are used for storing various calculation results
        boolean canLoad; //both are used when the drone is loading to stop it from loading when needed

        while (ordersInQueueOrDrone()){

            if(drone.getCurrentPosition().isStarting()){
                canLoad = true;

                waitForTurnAroundTime();

                if(isDuringWaitTime()){
                    //during the initial waiting period, the drone waits
                    // until it cannot add anymore orders before launch
                    while(canLoad && isDuringWaitTime()){
                        while(ordersAreAvailable(canLoad) && isDuringWaitTime()){
                            if(nextOrderDoesNotExceedWeightCapacity()){
                                loadOrder();
                            } else {
                                launchDrone(iteration, FIFO_SIM_ID);
                                canLoad = false;
                            }
                            //to stop the drone from trying to fill itself after the waiting period
                            if(currentTime == MINUTES_TO_WAIT_AT_START){
                                canLoad = false;
                            }
                        }
                        waitOneMinuteIfShouldKeepLoading(canLoad);
                    }

                } else {
                    //this launches the drone after the wait time if there is anything on it
                    // so that it doesn't try and wait around any more
                    if(currentTime >= MINUTES_TO_WAIT_AT_START + 1){
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        }
                    }

                    while(canLoad && currentOrder <= times.size() - 1){
                        while(ordersAreAvailable(canLoad)){
                            if(nextOrderDoesNotExceedWeightCapacity()){
                                loadOrder();
                            } else {
                                canLoad = false;
                            }
                        }
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        } else {
                            waitOneMinuteIfShouldKeepLoading(canLoad);
                        }
                    }
                    launchDrone(iteration, FIFO_SIM_ID);
                }
            } else {
                //the drone is not at the starting point and is empty
                if(drone.getNumOrders() == 0){
                    returnToStart();
                } else { //the drone is not home and has orders
                    //the drone figures out if it can make it to its next destination and home if it needs to charge
                    calcTime = calculateTime(drone.getCurrentPosition(), drone.getOrderOnDrone(0).getDestination());
                    homeTime = calculateTime(drone.getOrderOnDrone(0).getDestination(), simMap.getStartingPoint());

                    //the drone returns home if it cannot make it to the next stop and home
                    if(willExceedMaxFlightTime(calcTime, homeTime)){
                        returnToStart();
                    } else { //the drone continues its current delivery path
                        deliverOrder(iteration, FIFO_SIM_ID, calcTime);
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
        double calcTime, homeTime; //calcTime and homeTime are used for storing various calculation results
        int start, max, min, weight = 0; //tracks the current order in currentOrderQueue
        boolean canLoad, skip = false; //both launched and canLoad are used when the drone is loading to stop it from loading
        // when needed, skip is used when an order is skipped within the currentOrderQueue and added to the skipped queue
        ArrayList<Order> skippedList = new ArrayList<Order>(); //keeps a list of skipped orders
        ArrayList<Integer> skipped = new ArrayList<Integer>(); //stores the number of times skipped orders have been skipped in knapsack

        while ((ordersInQueueOrDrone() || skippedList.size() > 0)){

            if(drone.getCurrentPosition().isStarting()){
                canLoad = true;

                waitForTurnAroundTime();

                if(isDuringWaitTime()){
                    //the drone will add any available orders if it can. If the drone is full then it will launch
                    while(canLoad && isDuringWaitTime()){
                        while(ordersAreAvailable(canLoad) && isDuringWaitTime()){
                            if(nextOrderDoesNotExceedWeightCapacity()){
                                loadOrder();
                            } else {
                                launchDrone(iteration, KNAPSACK_SIM_ID);
                                canLoad = false;
                            }
                            if(currentTime == MINUTES_TO_WAIT_AT_START){
                                canLoad = false;
                            }
                        }

                        waitOneMinuteIfShouldKeepLoading(canLoad);
                    }

                    //when the drone is at the starting position and it's not within the first five minutes of the simulation
                } else {
                    //resets the pickup times for the orders that were loaded before the five minute mark
                    //this launches the drone at minute 6 if there is anything on it so that it doesn't try and wait around any more
                    if(currentTime >= MINUTES_TO_WAIT_AT_START + 1){
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        }
                    }
                    while(canLoad && currentOrder <= times.size() - 1){
                        max = 0;
                        start = 0;
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
                        while(ordersAreAvailable(canLoad)){//loads available orders until weight capacity is hit
                            //fixes an error where orders were getting stuck at the end if there was only one left
                            //stops order skipping whenever the orders can no longer fill the drone
                            weight = 0;
                            for(int i = 0; i < currentOrderQueue.size(); i++){
                                weight += currentOrderQueue.get(i).getMeal().getTotalWeight();
                                //prevents unneeded looping if we know the weight is already greater than capacity
                                if(weight > drone.getWeightCapacity()){
                                    i = currentOrderQueue.size();
                                }
                            }
                            if(weight <= drone.getWeightCapacity()){
                                hasSkipped = true;
                            }

                            //loop to find if the first order is the greatest
                            if(!hasSkipped && currentOrderQueue.size() > 1){
                                for(int r = 0; r < times.size() - currentOrder; r++){
                                    if(times.get(currentOrder + r) <= currentTime &&  currentOrderQueue.get(max).getMeal().getTotalWeight() < currentOrderQueue.get(r).getMeal().getTotalWeight()){ //index out of bounds 2
                                        max = r;
                                    }
                                }
                            }

                            //adds the largest order to the skipped queue
                            if(currentOrderQueue.size() > 1 && !hasSkipped){
                                if(times.get(currentOrder + 1) <= currentTime && drone.getCurrentWeight() + currentOrderQueue.get(1).getMeal().getTotalWeight() < drone.getWeightCapacity()){
                                    if(max == 0){
                                        skip = true;
                                        hasSkipped = true;
                                        currentOrderQueue.get(0).setPickUpTime(currentTime);
                                        skippedList.add(currentOrderQueue.get(0));
                                        currentOrderQueue.remove(0);
                                        skipped.add(1);
                                        currentOrder++;
                                    }
                                }
                            }

                            //adds the next first in the queue if it is available as we know it's not the largest
                            if(ordersAreAvailable(canLoad) && nextOrderDoesNotExceedWeightCapacity()){
                                loadOrder();
                            } else {
                                canLoad = false;
                            }
                        }

                        //adds any orders that were skipped that may fit into the drone before going to the next minute

                        start = 0;
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
                        } else { //the drone will wait for more orders
                            waitOneMinuteIfShouldKeepLoading(canLoad);
                        }
                    }

                    launchDrone(iteration, KNAPSACK_SIM_ID);
                }
            } else {
                //the drone is not home and is empty, so it goes back to the start point
                if(drone.getNumOrders() == 0){
                    returnToStart();
                } else { //the drone is not home and has orders
                    //the drone figures out if it can make it to its next destination and home if it needs to charge
                    calcTime = calculateTime(drone.getCurrentPosition(), drone.getOrderOnDrone(0).getDestination());
                    homeTime = calculateTime(drone.getOrderOnDrone(0).getDestination(), simMap.getStartingPoint());

                    //the drone returns home if it cannot make it to the next stop and home
                    if(willExceedMaxFlightTime(calcTime, homeTime)){
                        returnToStart();
                    } else { //the drone continues its current delivery path
                        deliverOrder(iteration, KNAPSACK_SIM_ID, calcTime);
                    }
                }
            }
        }
    }

    /*||-----------Utility Methods Between FIFO and Knapsack-----------||*/

    /**
     * Author: Patrick Reagan
     * Loads the first order onto the drone.
     */
    private void loadOrder(){
        currentOrderQueue.get(0).setPickUpTime(times.get(currentOrder));
        drone.addOrderToDrone(currentOrderQueue.get(0));
        currentOrderQueue.remove(0);
        currentOrder++;
    }

    //the drone runs its first route so it is no longer at the home waypoint
    private void launchDrone(int iteration, int simID){
        drone.setOrdersList(sortOrders(drone.getOrdersList()));
        double calcTime = calculateTime(simMap.getStartingPoint(),
                drone.getOrderOnDrone(0).getDestination());
        deliverOrder(iteration, simID, calcTime);
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
     * Delivers an order in either simulation. Taking in a simType eliminated the need of having two separate methods
     * @param iteration is used for finding which hour the order was available in
     * @param simID is used to identify whether it is a FIFO or Knapsack delivery time
     */
    private void deliverOrder(int iteration, int simID, double calcTime){
        int hourFrom = 1;
        currentTime += calcTime;
        tripTime += calcTime;

        //finds what hour the orders pickup time is from to ensure it is added to the correct list
        while(drone.getOrderOnDrone(0).getPickUpTime() - (MINUTES_PER_SHIFT * hourFrom) > 0){
            hourFrom++;
        }
        if(simID == FIFO_SIM_ID){
            drone.getFIFODeliveryTimesList().addDeliveryTime((hourFrom - 1) + (numShifts * iteration),
                    currentTime - drone.getOrderOnDrone(0).getPickUpTime());
        } else if (simID == KNAPSACK_SIM_ID){
            drone.getKnapsackDeliveryTimesList().addDeliveryTime((hourFrom - 1) + (numShifts * iteration),
                    currentTime - drone.getOrderOnDrone(0).getPickUpTime());
        }
        drone.setCurrentPosition(drone.getOrderOnDrone(0).getDestination());
        drone.removeOrderFromDrone(0);
    }

    private void returnToStart(){
        double calcTime = calculateTime(drone.getCurrentPosition(), simMap.getStartingPoint());
        currentTime += calcTime;
        tripTime += calcTime;
        drone.setCurrentPosition(simMap.getStartingPoint());
    }

    private boolean ordersAreAvailable(boolean canLoad){
        //broken apart to prevent trying to load orders that do not exist
        //and when it is allowed to load
        if(canLoad && currentOrder <= times.size() - 1){
            return (times.get(currentOrder) <= currentTime);
        } else {
            return false;
        }
    }

    private void waitOneMinuteIfShouldKeepLoading(boolean canLoad){
        if(times.get(currentOrder) > currentTime && canLoad){//increment time if not full
            currentTime++;
        }
    }

    private boolean willExceedMaxFlightTime(double calcTime, double homeTime){
        return (tripTime + calcTime + homeTime >=
                drone.getMaxFlightTime() * PERCENTAGE_OF_FLIGHT_TIME_NOT_TO_EXCEED);
    }

    private void waitForTurnAroundTime(){
        if(tripTime > 0){
            currentTime += drone.getTurnAroundTime();
            tripTime = 0;
        }
    }

    private boolean ordersInQueueOrDrone(){
        return (currentOrderQueue.size() > 0 || drone.getNumOrders() > 0);
    }

    private boolean isDuringWaitTime(){
        return (currentTime <= MINUTES_TO_WAIT_AT_START);
    }

    private boolean nextOrderDoesNotExceedWeightCapacity(){
        return (drone.getCurrentWeight() + currentOrderQueue.get(0).getMeal().getTotalWeight() < drone.getWeightCapacity());
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
    private double distance(Waypoint wp1, Waypoint wp2) {
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
    private void heapSort(ArrayList<Integer> a)
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
    private void createHeap(ArrayList<Integer> a, int root, int size)
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
