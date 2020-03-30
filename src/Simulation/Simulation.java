package Simulation;

import Food.*;
import Mapping.Waypoint;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    private ArrayList<Order> orderQueue;
    private ArrayList<Meal> mealList; //stores all different possible meals. Will be passed on constructor
    private ArrayList<Order> currentOrderQueue;
    private int numShifts, timesToBeRan; //passed in constructor, stores number of hours to do the simulation and number of dif sims
    public int[] ordersPerHour, times;

    //Creation of drone for testing purposes at this point
    public Drone drone = new Drone();

    //the check for adding the probabilities needs to be somewhere. Should be outside of this class
    //for simulations with default settings ie sprint 1
    public Simulation(){
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
            copyOrderQueue();

            //runFIFO();

            copyOrderQueue();

            //runKnapsack();
        }

        //drones must be reset before each simulation
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
        for(int i = 0; i < orderQueue.size(); i++){
            currentOrderQueue.add(orderQueue.get(i));
        }
    }

    private void runFIFO(){
        //runs FIFO simulations
        //at start, wait for 5 min or until full
        //go minute by minute. Update the current order queue with any new orders, see where the drone is. if its at the sac pick up orders.
        //if not then let it keep doing its thing. Maybe store the time for the drones current route
        double currentTime = 0;
        int remainingTransit = 0, overtime = 0, currentOrder; //remainingTransit will indicate how close a drone is to its current target position
        //overtime will add additional minutes to the simulation if there are any last minute orders
        //currentOrder tracks the current order. Will be used with the times array for checking

        while (currentTime < (numShifts * 60) + overtime){
            if(currentTime <= 5){ //and at the starting position (... && drone.getCurrentPosition())
                if(drone.getTurnAroundTime() == 0){
                    //loading phase if anything is available
                    //if conditions are met for the drone to leave, calculate tsp and start it on its route
                } else {
                    drone.setTurnAroundTime(drone.getTurnAroundTime() - 1);
                    currentTime++;
                }
            } /*else if(){//drone is at the starting position
                if(drone.getTurnAroundTime() == 0){
                    //loading phase if anything is available
                    //calculate tsp and start the drone
                } else {
                    drone.setTurnAroundTime(drone.getTurnAroundTime() - 1);
                    currentTime++;
                }
            }*/ else {//drove is not at the stating position
                if(remainingTransit == 0){
                    //if the drone is carrying nothing then it returns home
                    //else call tsp/a list of destinations already found by tsp to get the drones next route
                    //reset the drones target destination and the distance to get there
                } else if (remainingTransit > 0){

                }
            }

            if(currentTime >= (numShifts * 60) + overtime - 1){ //and if the current order queue is not empty and the drone is not empty
                //the drone also must not be home
                overtime++;
            }

            //the appropriate time needs added to the drone depending on if it delivered or traveled
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
        //will use the drones current location and current orders to solve the traveling salesman problem
        //This is the same as my main in BackTrackingTSP.java - Josh
        //the number of nodes i.e. V
        int numNodes = 0;
        double[][] points = new double[orders.size()][2];

        // Fill points with orders ArrayList
        for(int i = 0; i < orders.size(); i++) {
            double latitude = orders.get(i).getDestination().getLatitude();
            double longitude = orders.get(i).getDestination().getLongitude();
        }

        // need to get the list of points
        try {
            FileInputStream fis = new FileInputStream("graph.txt");
            Scanner scn = new Scanner(fis);

            // get the number of nodes
            numNodes = scn.nextInt();
            scn.nextLine();

            points = new double[numNodes][2];
            int currentNode = 0;

            // Fill points - OLD
            while(scn.hasNext()) {
                if(scn.hasNextDouble())
                    points[currentNode][0] = scn.nextDouble();
                else {
                    scn.close();
                    throw new Exception("Trouble getting x");
                }
                if(scn.hasNextDouble())
                    points[currentNode][1] = scn.nextDouble();
                else {
                    scn.close();
                    throw new Exception("Trouble getting y");
                }
                System.out.println("(" + points[currentNode][0] + ", " + points[currentNode][1] + ")");

                currentNode++;
                if(scn.hasNextLine())
                    scn.nextLine();
            }
            System.out.println("Total nodes: " + numNodes);

            scn.close();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        double[][] graph = new double[numNodes][numNodes];

        System.out.println("length: " + points.length);
        //Now that I have the points, I need to make the graph
        for(int node = 0; node < points.length; node++) {
            for(int otherNode = 0; otherNode < points.length; otherNode++) {
                //need to find the x and y distances between node and otherNode
                double deltaX = points[node][0] - points[otherNode][0];
                double deltaY = points[node][1] - points[otherNode][1];
                //We raise those distances to the second power
                double powX = Math.pow(deltaX, 2.0);
                double powY = Math.pow(deltaY, 2.0);
                //The distance between the points using the pythagorean theorem
                graph[node][otherNode] = (Math.sqrt(powX + powY));
            }
        }

        //For testing purposes, print the graph
        for(int i = 0; i < numNodes; i++) {
            for(int j = 0; j < numNodes; j++) {
                System.out.printf("%.2f ", graph[i][j]);
            }
            System.out.println("");
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

        System.out.println("BEST SOLUTION:");
        double bestCost = 0;
        for(int i = 1; i < bestSolution.length; i++) {
            int node1 = bestSolution[i - 1];
            int node2 = bestSolution[i];
            System.out.printf("%d >(+%.2f)> ", node1, graph[node1][node2]);
            if(i == bestSolution.length - 1)
                System.out.print(node2);
            bestCost += graph[node1][node2];
        }
        System.out.printf(", COST: %.4f\n", bestCost);

        //return bestSolution;
        return orders;
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
    //public Double average = getAverage(drone.deliveryTimes);
    //public Double worst = getWorst(drone.deliveryTimes);

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
