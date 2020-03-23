package Food;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {

    ArrayList<Order> orders;
    public int numShifts;
    public int [] ordersPerHour;
    public int totalOrders;

    public OrderGenerator(){
        //Grab number of hour shifts and place here
        this.numShifts = 4;
        this.ordersPerHour = new int[numShifts];
        //Add orders per hour to array then add that to totalOrders
        for(int i = 0; i<numShifts; i++){
            ordersPerHour[i] = 15;
            totalOrders += ordersPerHour[i];
        }
    }

    public void getOrders(){

    }
}


