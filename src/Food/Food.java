package Food;

/**
 * Authors: Patrick Reagan and Spencer Gray
 * Purpose: Simulates a food item.
 */

public class Food {
    public double weight; //stores the weight of a food item
    public String name; //stores the name of a food item

    /**
     * Creates a new food item with a specified weight and name.
     * @param n is the specified name for the food item.
     * @param w is the specified weight of the food item.
     */
    public Food(String n, double w) {
        name = n;
        weight = w;
    }

    /**
     * Returns the weight of a food item.
     * @return the weight of a food item.
     */
    public double getWeight(){
        return weight;
    }

    /**
     * Returns the name of a food item.
     * @return the name of a food item.
     */
    public String getName(){
        return name;
    }
}
