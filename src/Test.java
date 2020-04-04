import Simulation.Simulation;

public class Test {
    public static void main(String[] args) {
        /*double prob;
        for(int i = 0; i < 50; i++){
            prob = (Math.random()*(1 - 0)) + 0;
            System.out.println(prob);
        }*/

        Simulation sim = new Simulation();
        sim.runSimulation();
    }
}