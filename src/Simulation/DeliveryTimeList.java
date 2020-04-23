package Simulation;

import java.util.ArrayList;

public class DeliveryTimeList {
    public ArrayList<ArrayList<Double>> DeliveryTimesList;

    public DeliveryTimeList(){
        DeliveryTimesList = new ArrayList<ArrayList<Double>>();
    }

    /**
     * Adds a delivery time to a specified hour's list.
     * @param i is the index of the desired hour.
     * @param b is the delivery time being added.
     */
    public void addDeliveryTime(int i, double b){
        DeliveryTimesList.get(i).add(b);
    }

    /**
     * Returns a specified delivery time.
     * @param i is the index of the hour the delivery time is in.
     * @param j is the position in the hour's list of the delivery time.
     * @return the specified delivery time.
     */
    public double getDeliveryTime(int i, int j){
        return DeliveryTimesList.get(i).get(j);
    }

    /**
     * Returns the number of hours of stored times.
     * @return the number of hours of stored times.
     */
    public int getNumDeliveryTimes(){
        return DeliveryTimesList.size();
    }

    /**
     * Returns the number of delivery times stored for a specified hour.
     * @param i the index of the desired delivery time hour.
     * @return the the number of delivery times stored specified hour's delivery's list.
     */
    public int getNumDeliveryTimes(int i){
        return DeliveryTimesList.get(i).size();
    }

    /**
     * Returns the list containing the lists of each hours delivery times.
     * @return the list containing the lists of each hours delivery times.
     */
    public ArrayList<ArrayList<Double>> getDeliveryTimesList(){
        return DeliveryTimesList;
    }
}
