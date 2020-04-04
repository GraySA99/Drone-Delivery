package Graphing;

import Simulation.Simulation;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Random;

public class ResultsGraph extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Defining the x axis
        CategoryAxis xAxis = new CategoryAxis();
        ObservableList<String> array = FXCollections.observableArrayList();
        Simulation test = new Simulation();
        for(int i = 1; i<=test.times.length; i++) {
            array.add("" + i);
        }
        xAxis.setCategories(array);
        xAxis.setLabel("Meal #");

        //Defining the y axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Time");
        //Creating the Bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("FIFO Delivery Times");
        //Prepare XYChart.Series objects by setting data
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("FIFO");

        for(int i = 0; i<test.times.length; i++){
            Random rand = new Random();
            Double newDouble = rand.nextDouble();
            test.drone.deliveryTimes.add(newDouble);
            series1.getData().add(new XYChart.Data<>(""+(i+1), test.drone.deliveryTimes.get(i)));
        }

        //Setting the data to bar chart
        barChart.getData().addAll(series1);

        //Creating a Group object
        Group root = new Group(barChart);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        //Setting title to the Stage
        primaryStage.setTitle("Bar Chart");

        //Adding scene to the stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
    }
}
