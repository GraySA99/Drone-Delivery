package Food;

import java.util.ArrayList;

public class Meal {
    private double probability, totalWeight;
    private ArrayList<Food> foodList = new ArrayList<Food>();

    public Meal(){
        //any constructors that create meals with items in them (can have them take an array of food) must add up the weight
    }

    /**
     * Parses through the foodList array and calculates the total weight of the current meal.
     */
    private void calculateWeight(){
        double sum = 0;
        for(int i = 0; i < foodList.size(); i++){
            sum += foodList.get(i).getWeight();
        }

        totalWeight = sum;
    }

    public void addItem(Food f){
        foodList.add(f);
        calculateWeight();
    }

    public void removeItem(int i){
        foodList.remove(i);
        calculateWeight();
    }

    public double getTotalWeight(){
        return totalWeight;
    }

    public double getProbability(){
        return probability;
    }

    public void setProbablity(double p){
        probability = p;
    }
}
