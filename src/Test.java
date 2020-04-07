import Simulation.Simulation;

public class Test {
    public static void main(String[] args) {
        /*double prob;
        for(int i = 0; i < 50; i++){
            prob = (Math.random()*(1 - 0)) + 0;
            System.out.println(prob);
        }*/

        //int a = 3/2, b = 4/2, c = 5/2;
        //1, 2, 2
        //System.out.println(a + " " + b + " " + c);

        Simulation sim = new Simulation();
        sim.runSimulation();
    }
}