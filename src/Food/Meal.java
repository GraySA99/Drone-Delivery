package Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Meal {
    private double probability, totalWeight;
    private HashMap<Food, Integer> foodList = new HashMap<>();
    private String name;


    public Meal(){
        totalWeight = 0;
        probability = 0;
        foodList = new HashMap<Food, Integer>();
    }

    public Meal(ArrayList<Food> list, double p){
        totalWeight = 0;
        probability = p;
        foodList = new HashMap<Food, Integer>();

        for(int i = 0; i < list.size(); i++){
            //foodList.put(list.get(i));
            totalWeight += list.get(i).getWeight();
        }
    }

    public Meal(String n, HashMap<Food, Integer> fList, double prob) {

        name = n;
        foodList = fList;
        probability = prob;
        calculateWeight();
    }

    /**
     * Parses through the foodList array and calculates the total weight of the current meal.
     */
    private void calculateWeight(){
        double sum = 0;
        for (Food food: foodList.keySet()) {

            sum += food.getWeight();
        }

        totalWeight = sum;
    }

    public void addItem(Food f){
        foodList.put(f, 1);
        calculateWeight();
    }

    public Set<Food> getFoodItems() {

        return foodList.keySet();
    }

    public int getFoodItemQty(Food food) {

        return foodList.get(food);
    }

    public boolean hasFoodItem(Food f) {

        for (Food food : foodList.keySet()) {

            if (food.getName().equals(f)) {

                return true;
            }
        }

        return false;

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

    public String getName() { return name; }
}
