package Simulation;

import Food.Food;
import Food.Meal;
import Mapping.Waypoint;

import java.util.ArrayList;
import java.util.HashMap;

public class DataTransfer {

    private static ArrayList<Waypoint> deliveryPoints = new ArrayList<>();
    private static ArrayList<Food> foodItems = new ArrayList<>();
    private static ArrayList<Meal> meals = new ArrayList<>();
    private static HashMap<Integer, Integer> shifts = new HashMap<>();
    private static int numSims = -1;

    public static void debugToString() {

        System.out.println("Delivery Points: ");
        for (Waypoint wp : deliveryPoints) {

            System.out.println(String.format("%s %f %f", wp.getName(), wp.getLatitude(), wp.getLongitude()));
        }

        System.out.println("\n\nFood Items: ");
        for (Food food : foodItems) {

            System.out.println(String.format("%s %f", food.getName(), food.getWeight()));
        }

        System.out.println("\n\nMeals: ");
        for (Meal meal : meals) {

            System.out.println(String.format("%s %f", meal.getName(), meal.getProbability()));
            for (Food food : meal.getFoodItems()) {

                System.out.println(String.format("\t%s %f %d", food.getName(), food.getWeight(),
                        meal.getFoodItemQty(food)));
            }
        }

        System.out.println("\n\nShifts: ");
        for (int i : shifts.keySet()) {

            System.out.println(String.format("%d %d", i, shifts.get(i)));
        }
    }

    public static boolean addShift(Integer index, Integer numOrder) {

        // Replace if exists
        if (shifts.get(index) != null) {

            shifts.replace(index, numOrder);
        } else {

            shifts.put(index, numOrder);
        }

        return true;
    }

    public static Integer removeShift(Integer index) {

        if (shifts.get(index) != null)
            return shifts.remove(index);
        return null;
    }

    public static Integer getNumShifts() {

        return shifts.size();
    }

    public static int getNumSimulations() {

        return numSims;
    }

    public static void setNumSimulations(int num) {

        numSims = num;
    }

    public static Integer getShift(Integer index) {

        return shifts.get(index);
    }

    public static int[] getShifts() {

        int[] list = new int[shifts.size()];

        int index = 0;
        for (int i : shifts.values()) {
            list[index++] = i;
        }

        return list;
    }

    public static boolean addMeal(Meal meal) {

        if (meals == null)
            meals = new ArrayList<>();

        return meals.add(meal);
    }

    public static Meal removeMeal() {

        return meals.remove(0);
    }

    public static boolean removeMeal(Meal meal) {

        return meals.remove(meal);
    }

    public static Meal removeMeal(int index) {

        return meals.remove(index);
    }

    public static Meal getMeal() {

        return meals.get(0);
    }

    public static Meal getMeal(int index) {

        return meals.get(index);
    }

    public static Meal getMeal(String name) {

        for (Meal meal : meals) {

            if (meal.getName().equals(name)) {

                return meal;
            }
        }

        return null;
    }

    public static boolean addWaypoint(Waypoint wp) {

        if (deliveryPoints == null)
            deliveryPoints = new ArrayList<>();

        return deliveryPoints.add(wp);
    }

    public static Waypoint removeWaypoint() {

        return deliveryPoints.remove(0);
    }

    public static boolean removeWaypoint(Waypoint wp) {

        return deliveryPoints.remove(wp);
    }

    public static Waypoint removeWaypoint(int index) {

        return deliveryPoints.remove(index);
    }

    public static Waypoint removeWaypoint(String name) {

        for (Waypoint wp : deliveryPoints) {

            if (wp.getName().equals(name)) {

                deliveryPoints.remove(wp);
                return wp;
            }
        }

        return null;
    }

    public static Waypoint getWaypoint(String name) {

        for (Waypoint wp : deliveryPoints) {

            if (wp.getName().equals(name)) { return wp; }
        }

        return null;
    }

    public static Waypoint getWaypoint() {

        return deliveryPoints.get(0);
    }

    public static Waypoint getWaypoint(int index) {

        return deliveryPoints.get(index);
    }

    public static boolean addFood(Food food) {

        if (foodItems == null)
            foodItems = new ArrayList<>();

        return foodItems.add(food);
    }

    public static Food removeFood() {

        return foodItems.remove(0);
    }

    public static boolean removeFood(Food food) {

        return foodItems.remove(food);
    }

    public static Food removeFood(int index) {

        return foodItems.remove(index);
    }

    public static Food getFood() {

        return foodItems.get(0);
    }

    public static Food getFood(int index) {

        return foodItems.get(index);
    }

    public static Food getFood(String name) {

        for (Food food : foodItems) {

            if (food.getName().equals(name)) {

                return food;
            }
        }

        return null;
    }

    public static Meal[] getMeals() {

        if (meals == null)
            return null;

        Meal[] temp = new Meal[meals.size()];

        int i = 0;
        for (Meal m : meals) {

            temp[i++] = m;
        }

        return temp;
    }

    public static Waypoint[] getWaypoints() {

        if (deliveryPoints == null)
            return null;

        Waypoint[] temp = new Waypoint[deliveryPoints.size()];

        int i = 0;
        for (Waypoint wp : deliveryPoints) {

            temp[i++] = wp;
        }

        return temp;
    }

    public static Food[] getFoodItems() {

        if (foodItems == null)
            return null;

        Food[] temp = new Food[foodItems.size()];

        int i = 0;
        for (Food f : foodItems) {

            temp[i++] = f;
        }

        return temp;
    }
}
