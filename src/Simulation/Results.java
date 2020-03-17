package Simulation;

public class Results {
    public static float getAverage(float [] times){
        float sum = 0;
        for(int i = 0; i<times.length; i++){
            sum += times[i];
        }
        return sum/times.length;
    }
    public static float getWorst(float [] times){
        float worst = 0;
        for(int i = 0; i<times.length; i++){
            if(times[i]>worst){
                worst = times[i];
            }
        }
        return 0;
    }
}
