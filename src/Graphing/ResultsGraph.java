package Graphing;

import Simulation.Drone;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ResultsGraph extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Defining the x axis
        CategoryAxis xAxis = new CategoryAxis();
        ObservableList<String> array = FXCollections.observableArrayList();
        for(int i = 1; i<60; i++){
            array.add(""+i);
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
        Drone drone = new Drone();
        for(int i = 0; i<drone.deliveryTimes.length; i++){
            Random rand = new Random();
            Double newDouble = rand.nextDouble();
            drone.deliveryTimes[i] = newDouble;
        }
        for(int i = 0; i<drone.deliveryTimes.length; i++){
            series1.getData().add(new XYChart.Data<String, Number>("Meal #", drone.deliveryTimes[i]));
        }

        //Setting the data to bar chart
        barChart.getData().addAll(series1);
        Group root = new Group(barChart);
        Scene scene = new Scene(root ,600, 300);
        primaryStage.setTitle("Graphing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
