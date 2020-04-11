package Food;

import Mapping.Waypoint;

/**
 * Author: Patrick Reagan
 * Purpose: A class used to simulate an order. Has an associated meal, Waypoint destination, and pick up time.
 */

public class Order{
    private Waypoint destination; //stores the orders destination
    private Meal orderMeal; //stores the order's meal
    private double pickUpTime; //stores the order's pick up time

    /**
     * Creates a new order with a specified meal and destination.
     * @param m is the specified meal.
     * @param w is the specified destination.
     */
    public Order(Meal m, Waypoint w){
        destination = w;
        orderMeal = m;
        pickUpTime = 0;
    }

    /**
     * Creates a default order with no given meal or destination.
     */
    public Order(){
        destination = null;
        orderMeal = null;
        pickUpTime = 0;
    }

    /**
     * Returns the destination of an order.
     * @return the destination of an order.
     */
    public Waypoint getDestination(){
        return destination;
    }

    /**
     * Returns the meal of an order.
     * @return the meal of an order.
     */
    public Meal getMeal(){
        return orderMeal;
    }

    /**
     * Returns the pick up time of an order.
     * @return the pick up time of an order.
     */
    public double getPickUpTime(){
        return pickUpTime;
    }

    /**
     * Sets the pick up time of an order.
     * @param t is the time the pickup time will be set to.
     */
    public void setPickUpTime(double t){
        pickUpTime = t;
    }

    /**
     * Sets the meal of an order.
     * @param m is the meal the order's will be set to.
     */
    public void setMeal(Meal m){
        this.orderMeal = m;
    }

    /**
     * Sets the destination of an order.
     * @param destination is the destination the order's will be set to.
     */
    public void setDestination(Waypoint destination) {
        this.destination = destination;
    }
}
