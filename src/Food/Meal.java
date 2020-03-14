package Food;

import java.util.ArrayList;

public class Meal {
    private double probability, totalWeight;
    private ArrayList<Food> foodList = new ArrayList<Food>();

    public Meal(){
        //any constructors that create meals with items in them (can have them take an array of food) must add up the weight
    }

    private void calculateWeight(){
        //parses through array and adds the weight of all food items
    }

    public void addItem(){
        //will need to call calculate weight at the end
    }

    public double getTotalWeight(){
        return totalWeight;
    }

    public double getProbability(){
        return probability;
    }
}
