package Food;

import java.util.ArrayList;

public class Meal {
    private double probability, totalWeight;
    private ArrayList<Food> foodList;

    public Meal(){
        totalWeight = 0;
        probability = 0;
        foodList = new ArrayList<Food>();
    }

    public Meal(ArrayList<Food> list, double p){
        totalWeight = 0;
        probability = p;
        foodList = new ArrayList<Food>();

        for(int i = 0; i < list.size(); i++){
            foodList.add(list.get(i));
            totalWeight += list.get(i).getWeight();
        }
    }

    /**
     * Parses through the foodList array and calculates the total weight of the current meal.
     */

    public void addItem(Food f){
        foodList.add(f);
        totalWeight += f.getWeight();
    }

    public void removeItem(int i){
        totalWeight -= foodList.get(i).getWeight();
        foodList.remove(i);
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
