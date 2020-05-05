package GUI;

/**
 ** This class refreshes all the values of the at a fixed rate to ensure everything loads properly
 **/

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
