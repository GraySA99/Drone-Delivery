package Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Authors: Patrick Reagan and Spencer Gray
 * Purpose: simulates a meal that is composed of different food items.
 */

public class Meal {
    private double probability, totalWeight; //stores the weight and probability of appearing for the meal
    private HashMap<Food, Integer> foodList; //the list of food items in the meal
    private String name; //the name of the meal

    /**
     * Creates a new default meal.
     */
    public Meal(){
        totalWeight = 0;
        probability = 0;
        foodList = new HashMap<Food, Integer>();
    }

    /**
     * Creates a meal using a list of specified list of food items and probability.
     * @param list is the list of food items.
     * @param p is the probability for the meal.
     */
    public Meal(ArrayList<Food> list, double p){
        totalWeight = 0;
        probability = p;
        foodList = new HashMap<Food, Integer>();

        for(int i = 0; i < list.size(); i++){ //I don't think this accounting for more than one food of a given type
            foodList.put(list.get(i), 1);
            totalWeight += list.get(i).getWeight();
        }
    }

    /**
     * Creates a meal using a list of specified list of food items, name, and probability.
     * @param n is the name for the meal
     * @param list is the list of food items.
     * @param p is the probability for the meal.
     */
    public Meal(String n, ArrayList<Food> list, double p){
        totalWeight = 0;
        probability = p;
        foodList = new HashMap<Food, Integer>();
        name = n;

        for(int i = 0; i < list.size(); i++){ //I don't think this accounting for more than one food of a given type
            foodList.put(list.get(i), 1);
            totalWeight += list.get(i).getWeight();
        }
    }

    /**
     * Creates a meal using a list of specified HashMap of food items, name, and probability.
     * @param n is the name for the meal.
     * @param fList is the HashMap of food items for the meal.
     * @param prob is the probability for the meal.
     */
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

    /**
     * Adds specified food item to the meal.
     * @param f is the specified food item.
     */
    public void addItem(Food f){
        foodList.put(f, 1);
        calculateWeight();
    }

    /**
     * Returns a set of food items.
     * @return a set of food items.
     */
    public Set<Food> getFoodItems() {

        return foodList.keySet();
    }

    /**
     * Returns the quantity of a specified food in the meal.
     * @param food is the specified food.
     * @return
     */
    public int getFoodItemQty(Food food) {

        return foodList.get(food);
    }

    /**
     * Returns if a specified food item is within the meal.
     * @param f is the specified food item.
     * @return true or false depending on if the specified item is within the meal.
     */
    public boolean hasFoodItem(Food f) {

        for (Food food : foodList.keySet()) {

            if (food.getName().equals(f)) {

                return true;
            }
        }

        return false;

    }

    /**
     * Return the total weight of the meal.
     * @return the total weight of the meal.
     */
    public double getTotalWeight(){
        return totalWeight;
    }

    /**
     * Returns the probability of a meal occurring.
     * @return the probability of a meal occurring.
     */
    public double getProbability(){
        return probability;
    }

    /**
     * Sets the probability of the meal to a specified value.
     * @param p the specified probability.
     */
    public void setProbablity(double p){
        probability = p;
    }

    /**
     * Returns the name of the meal.
     * @return the name of the meal.
     */
    public String getName() { return name; }
}
