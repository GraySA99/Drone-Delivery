package Simulation;

import Food.Food;
import Food.Meal;
import Food.Order;
import Mapping.Map;
import Mapping.Waypoint;

import java.util.ArrayList;

public class Simulation {
    private ArrayList<Order> orderQueue; //stores the order queue for a pair of simulations
    private ArrayList<Meal> mealList; //stores all different possible meals. Will be passed on constructor
    private ArrayList<Order> currentOrderQueue; //for knapsack, skipped orders are prioritized and to a priority list
    private int numShifts, timesToBeRan; //passed in constructor, stores number of hours to do the simulation and number of dif sims
    public int[] ordersPerHour;
    public ArrayList<Integer> times, skipped;
    private Map simMap;

    //Public variables for average time and worst time in the simulation, calculated at results
    public Double FIFOaverageTime;
    public Double KSaverageTime;
    public Double FIFOworstTime;
    public Double KSworstTime;

    //Creation of drone for testing purposes at this point
    public Drone drone;

    //Getter for numShifts
    public int getNumShifts(){
        return numShifts;
    }


    public Simulation(){
        ArrayList<Waypoint> testPoints = new ArrayList<Waypoint>();
        testPoints.add(new Waypoint("Point 1", 1, 1, true));
        testPoints.add(new Waypoint("Point 2", 2, 1, true));
        testPoints.add(new Waypoint("Point 3", 3, 1, true));
        simMap = new Map(new Waypoint("Starting", 0, 0, true), testPoints);
        drone = new Drone();
        orderQueue = new ArrayList<Order>();
        mealList = new ArrayList<Meal>();
        currentOrderQueue = new ArrayList<Order>();

        numShifts = 4;
        ordersPerHour = new int[numShifts];
        timesToBeRan = 1;
        ordersPerHour[0] = 15;
        ordersPerHour[1] = 17;
        ordersPerHour[2] = 22;
        ordersPerHour[3] = 15;

        times = new ArrayList<Integer>();
        skipped = new ArrayList<Integer>();

        ArrayList<Food> temp = new ArrayList<Food>();
        temp.add(new Food("Hamburger", 0.375));
        temp.add(new Food("Drink", 0.875));
        temp.add(new Food("Fries", 0.25));

        mealList.add(new Meal("One of Each", temp, 0.55));

        temp.add(new Food("Hamburger", 0.375));
        mealList.add(new Meal("Two burgers, one drink, one fry", temp, 0.1));

        temp.remove(2);
        temp.remove(2);
        mealList.add(new Meal("Burger and Drink", temp, 0.2));

        temp.add(new Food("Hamburger", 0.375));
        mealList.add(new Meal("Two Burgers and a Drink", temp, 0.15));
    }

    //the check for adding the probabilities needs to be somewhere. Should be outside of this class
    //for simulations with default settings ie sprint 1
    public Simulation(Map m){
        simMap = m;
        orderQueue = new ArrayList<Order>();
        mealList = new ArrayList<Meal>();
        currentOrderQueue = new ArrayList<Order>();
        drone = new Drone();

        numShifts = 4;
        ordersPerHour = new int[numShifts];
        timesToBeRan = 1;
        ordersPerHour[0] = 15;
        ordersPerHour[1] = 17;
        ordersPerHour[2] = 22;
        ordersPerHour[3] = 15;

        times = new ArrayList<Integer>();
        skipped = new ArrayList<Integer>();

        ArrayList<Food> temp = new ArrayList<Food>();
        temp.add(new Food("Hamburger", 0.375));
        temp.add(new Food("Drink", 0.875));
        temp.add(new Food("Fries", 0.25));

        mealList.add(new Meal(temp, 0.55));

        temp.add(new Food("Hamburger", 0.375));
        mealList.add(new Meal(temp, 0.1));

        temp.remove(2);
        temp.remove(2);
        mealList.add(new Meal(temp, 0.2));

        temp.add(new Food("Hamburger", 0.375));
        mealList.add(new Meal(temp, 0.15));
    }

    public Simulation(int numberOfShifts, int timesToRun, ArrayList<Meal> listOfMeals, int[] avgOrdersEachHour, Map m){
        //this will be used when we start having settings for the simulation
        numShifts = numberOfShifts;
        timesToBeRan = timesToRun;
        //deep copy of meals list

    }

    public void runSimulation(){
        //should add default meals (for now) to meal list
        //is what calls the run FIFO and runKnapsack methods a specified amount of times
        //calls generateOrderQueue and copy orderQueue when appropriate

        for(int i = 0; i < timesToBeRan; i++){
            generateOrderQueue();
            copyOrderQueue();

            drone.setCurrentPosition(simMap.getStartingPoint());
            runFIFO();
            drone.addFIFODeliveryTime(-1);

            copyOrderQueue();

            drone.setCurrentPosition(simMap.getStartingPoint());
            //runKnapsack();
        }

        for(int i = 0; i < drone.getNumFIFODeliveryTimes(); i++){
            System.out.print(drone.getFIFODeliveryTime(i) + " ");
        }

        //drones must be reset before each simulation
    }

    /**
     * Generates a list of random orders and puts them in the simulations order queue. Uses the probabilities for each order to appear and
     * randomly selects a point on the map to generate the specified number of orders in each hour using the meals available in the meals list.
     */
    private void generateOrderQueue(){
        int count = 0, curPos, mapPos;
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

        //clear out skipped list
        for(int r = skipped.size(); r > 0; r--){
            skipped.remove(r - 1);
        }

        for(int i = 0; i < numShifts; i++){
            for(int j = 0; j < ordersPerHour[i]; j++){
                curProb = 0;
                curPos = 0;
                found = false;

                //generates orders according to their given probabilities
                prob = (Math.random()*(1));
                while(curPos < mealList.size() && !found){
                    //System.out.println("The probability is " + prob + "\tThe Current pos is " + curPos + "\t Im checking if it's under " + mealList.get(curPos).getProbability() + curProb + "\twhich equals " + mealList.get(curPos).getProbability() + "+" + curProb);
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
                times.add((i * 60) + (int) (1 + Math.random() * ((60 - 1) + 1)));
                skipped.add(0);
                count++;
            }
        }

        //sorts the times array from least to greatest
        heapSort(times);

        for(int r = 0; r < orderQueue.size(); r++){
            System.out.println("At time " + times.get(r) + " An order with " + orderQueue.get(r).getMeal().getName() + " to " + orderQueue.get(r).getDestination().getName() + " will appear in the queue.");
        }
    }

    /**
     * Creates a deep copy of the orderQueue and stores it in currentOrderQueue to allow modifications during a simulation
     */
    private void copyOrderQueue(){
        for(int i = 0; i < orderQueue.size(); i++){
            currentOrderQueue.add(orderQueue.get(i));
        }
    }

    //To-do:
    //Waiting for corrected distance calcs in tsp
    //Ask about charging and turn around

    //made assuming that a drone charge must occur all at one time
    private void runFIFO(){
        //runs FIFO simulations

        double currentTime = 0, tripTime = 0, calcTime, waitedTime, homeTime;
        int currentOrder = 0, prevResult = 1; //tracks the current order in currentOrderQueue
        boolean launched = false, canLoad = true;

        while (currentOrderQueue.size() > 0 || drone.getNumOrders() > 0){
            System.out.println("The queue size is " + currentOrderQueue.size() + "\tThe number of orders on the drone is " + drone.getNumOrders());
            if(drone.getCurrentPosition().isStarting()){
                launched = false;
                canLoad = true;
                waitedTime = 0;

                if(tripTime > 0){
                    currentTime += drone.getTurnAroundTime();
                    tripTime = 0;
                    System.out.println("Upon returning to the start the drone needed 3 min to recharge and load. The current time is " + currentTime);
                }

                //when the drone is at the starting point and it's within the first five minutes of the simulation
                if(currentTime <= 5){
                    //the drone will add any available orders if it can. If the drone is full then it will launch
                    while(!launched && currentTime <= 5){
                        while(times.get(currentOrder) <= currentTime && !launched && currentTime <= 5){
                            if(drone.getCurrentWeight() + currentOrderQueue.get(0).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                System.out.print("Added order to drone that was available at time " + times.get(currentOrder) + " at time " + currentTime + "\t");
                                int a = 1;
                                while(times.get(currentOrder) - (60 * a) > 0){
                                    a++;
                                }
                                if(a != prevResult){
                                    drone.addFIFODeliveryTime(-1);
                                    prevResult = a;
                                }
                                System.out.println("\n" + a + "\n");
                                loadOrder(currentOrder);
                                currentOrder++;
                            } else { //launch
                                drone.setOrdersList(sortOrders(drone.getOrdersList()));
                                //resets the pickup times as the drone did not really leave until this point
                                for(int i = 0; i < drone.getNumOrders(); i++){
                                    drone.getOrderOnDrone(i).setPickUpTime(currentTime);
                                }
                                System.out.print("Began launch sequence. TSP has been called.");
                                calcTime = calculateTime(simMap.getStartingPoint(), drone.getOrderOnDrone(0).getDestination());
                                System.out.print(" The calculated time is " + calcTime + " while old current time is " + currentTime);
                                currentTime += calcTime;
                                tripTime += calcTime;
                                System.out.print(" The new time is " + currentTime + " while current trip time is " + tripTime + "\n");
                                deliverOrder(currentTime);
                                launched = true;
                            }
                            if(currentTime == 5){
                                launched = true;
                            }
                        }

                        if(times.get(currentOrder) > currentTime && !launched){//increment time if not full
                            System.out.println("The drone waited a minute for more orders to appear.");
                            currentTime++;
                            waitedTime++;
                            /*if(waitedTime >= 3){//may need changed depending on what Valentine wants
                                tripTime = 0;
                            }*/
                        }
                    }

                    //when the drone is at the starting position and it's not within the first five minutes of the simulation
                } else {
                    //resets the pickup times for the orders that were loaded before the five minute mark
                    if(currentTime < 7){
                        if(drone.getCurrentWeight() > 0){
                            canLoad = false;
                        }
                    }
                    while(canLoad && currentOrder <= times.size() - 1){
                        while(times.get(currentOrder) <= currentTime && canLoad){//loads available orders until weight capacity is hit
                            if(drone.getCurrentWeight() + currentOrderQueue.get(0).getMeal().getTotalWeight() < drone.getWeightCapacity()){ //check weight
                                System.out.print("Added order to drone that was available at time " + times.get(currentOrder) + " at time " + currentTime + "\t");
                                int a = 1;
                                while(times.get(currentOrder) - (60 * a) > 0){
                                    a++;
                                }
                                if(a != prevResult){
                                    drone.addFIFODeliveryTime(-1);
                                    prevResult = a;
                                }
                                System.out.println("\n" + a + "\n");
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
                            System.out.println("The drone waited a minute for more orders to appear.");
                            currentTime++;
                            waitedTime++;
                            /*if(waitedTime >= 3){ //may need changed depending on what Valentine wants
                                tripTime = 0;
                            }*/
                        }
                    }


                    drone.setOrdersList(sortOrders(drone.getOrdersList()));
                    System.out.print("Began launch sequence. TSP has been called.");
                    calcTime = calculateTime(simMap.getStartingPoint(), drone.getOrderOnDrone(0).getDestination());
                    System.out.print(" The calculated time is " + calcTime + " while old current time is " + currentTime);
                    currentTime += calcTime;
                    tripTime += calcTime;
                    System.out.print(" The new time is " + currentTime + " while current trip time is " + tripTime + "\n");
                    deliverOrder(currentTime);

                }
            } else {
                //the drone is not home and is empty
                if(drone.getNumOrders() == 0){
                    //add a time tracker for the current trip to make sure it won't surpass 20 min. Should check next route time and time to get home

                    calcTime = calculateTime(drone.getCurrentPosition(), simMap.getStartingPoint());
                    System.out.print("The drone has run out of orders and has returned home. The calc time was " + calcTime + " the old current time is " + currentTime);
                    currentTime += calcTime;
                    System.out.print(" And the new current time is " + currentTime);
                    tripTime += calcTime;
                    drone.setCurrentPosition(simMap.getStartingPoint());
                } else { //the drone is not home and has orders
                    //the drone figures out if it can make it to its next destination and home if it needs to charge
                    calcTime = calculateTime(drone.getCurrentPosition(), drone.getOrderOnDrone(0).getDestination());
                    homeTime = calculateTime(drone.getOrderOnDrone(0).getDestination(), simMap.getStartingPoint());

                    //the drone returns home
                    if(tripTime + calcTime + homeTime > drone.getMaxFlightTime() - 0.5){
                        System.out.print("After " + tripTime + " min of trip time, the drone has decided to return home.");
                        calcTime = calculateTime(drone.getCurrentPosition(), simMap.getStartingPoint());
                        System.out.print("The calc time was" + calcTime + " the old current time is " + currentTime + "\n");
                        currentTime += calcTime;
                        System.out.print(" And the new current time is " + currentTime);
                        drone.setCurrentPosition(simMap.getStartingPoint());
                        tripTime = 0;
                    } else { //the drone continues its current delivery path
                        currentTime += calcTime;
                        tripTime += calcTime;
                        System.out.print(" The new time is " + currentTime + " while current trip time is " + tripTime + "\n");
                        deliverOrder(currentTime);
                    }
                }
            }
        }
    }

    //To-do:
    //Create loading method
    private void runKnapsack(){
        //runs knapsack simulations
        int currentTime = 0;
        //assign a value to every order in queue, grab the heaviest and put it in the bag, then next heaviest, then so forth. if it fits, put it in.
        //skipped me counter, make sure skipped meals are prioritized
        while (currentTime < numShifts * 60){
            //fifo
        }
    }

    //||-----------Shared Utility Methods Between FIFO and Knapsack-----------||
    private void loadOrder(int currentOrder){
        currentOrderQueue.get(0).setPickUpTime(times.get(currentOrder));
        System.out.print("Set Pickup time for order " + currentOrderQueue.get(0).getMeal().getName() + " as " + currentOrderQueue.get(0).getPickUpTime() + "\n");
        drone.addOrderToDrone(currentOrderQueue.get(0));
        currentOrderQueue.remove(0);
    }

    private double calculateTime(Waypoint a, Waypoint b){
        System.out.println("The Calculated distance it " + distance(a, b));
        return 60 * ((distance(a, b) * 1) / (drone.getSpeed() * 63360 * 2.54 * (1 * Math.pow(10, -3)) )) + .5;
    }

    private void deliverOrder(double currentTime){
        drone.addFIFODeliveryTime(currentTime - drone.getOrderOnDrone(0).getPickUpTime());
        drone.setCurrentPosition(drone.getOrderOnDrone(0).getDestination());
        System.out.println("The drone delivered an order of " + drone.getOrderOnDrone(0).getMeal().getName() + " that was picked up at "
         + drone.getOrderOnDrone(0).getPickUpTime() + " and had a delivery time of " + drone.getFIFODeliveryTime(drone.getNumFIFODeliveryTimes() - 1) + " and has been removed " +
                "from the drone.");
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
