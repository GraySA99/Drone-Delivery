package Food;

public class Food {
    public double weight;
    public String name;

    public Food(String n, double w) {

        name = n;
        weight = w;
    }

    public double getWeight(){
        return weight;
    }

    public String getName(){
        return name;
    }
}
