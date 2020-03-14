package Food;

import Mapping.Waypoint;

public class Order {
    private Waypoint destination;
    private Meal orderMeal;

    public Waypoint getDestination(){
        return destination;
    }

    public Meal getMeal(){
        return orderMeal;
    }
}
