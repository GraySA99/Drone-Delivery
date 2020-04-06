package Food;

import Mapping.Waypoint;

public class Order{
    private Waypoint destination;
    private Meal orderMeal;
    private double pickUpTime;

    public Order(Meal m, Waypoint w){
        destination = w;
        orderMeal = m;
        pickUpTime = 0;
    }

    public Waypoint getDestination(){
        return destination;
    }

    public Meal getMeal(){
        return orderMeal;
    }

    public double getPickUpTime(){
        return pickUpTime;
    }

    public void setPickUpTime(double t){
        pickUpTime = t;
    }
}
