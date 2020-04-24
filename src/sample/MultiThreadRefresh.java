package sample;

public class MultiThreadRefresh extends Thread {

    public void run() {

        try {

            Thread.sleep(350);
            Values.main.refresh();
            Thread.sleep(500);
            Values.main.refresh();

        } catch (Exception e) {

            System.out.println("Error in multi-thread");
            e.printStackTrace();
        }

    }
}
