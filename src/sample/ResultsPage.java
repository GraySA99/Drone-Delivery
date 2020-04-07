package sample;

import Simulation.Drone;
import Simulation.Simulation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ResultsPage extends VBox {

    //Figure out how to get the most recent of each drone and bring them into this page
    Simulation recentSimulation = new Simulation();
    Drone droneforresults = recentSimulation.drone;

    //Average and worst delivery time variable, found by getting the average and worst from deliveryTimes
    //public Double average = getAverage(drone.deliveryTimes);
    //public Double worst = getWorst(drone.deliveryTimes);

    public static Double getAverage(ArrayList<Double> times){
        double sum = 0;
        for(int i = 0; i<times.size(); i++){
            sum += times.get(i);
        }
        return sum/times.size();
    }
    public static Double getWorst(ArrayList<Double> times){
        double worst = 0;
        for(int i = 0; i<times.size(); i++){
            if(times.get(i)>worst){
                worst = times.get(i);
            }
        }
        return worst;
    }

    public ResultsPage() {

        super();

        recentSimulation.runSimulation();

        HBox resultsContainer = new HBox();

        // Left
        VBox resultsBarChartFrame = new VBox();
        CategoryAxis resultsXAxis = new CategoryAxis();
        resultsXAxis.setLabel("Hours");
        NumberAxis resultsYAxis = new NumberAxis();
        resultsYAxis.setLabel("Delivery Times");
        BarChart<String, Number> resultsBarChart = new BarChart<String, Number>(resultsXAxis, resultsYAxis);
        resultsBarChart.setTitle("Results");
        XYChart.Series<String, Number> FIFOSeries = new XYChart.Series<>();
        FIFOSeries.setName("FIFO");
        //For loop iterating over the FIFO delivery times and adding each of them to the series

        for(int FIFOIndex = 1; FIFOIndex<=droneforresults.FIFODeliveryTimes.size(); FIFOIndex++){
            FIFOSeries.getData().add(new XYChart.Data<String,Number>(Integer.toString(FIFOIndex), droneforresults.FIFODeliveryTimes.get(FIFOIndex-1)));
        }

        XYChart.Series<String, Number> KSSeries = new XYChart.Series<>();
        KSSeries.setName("Knapsack");
        //For loop iterating all over the knapsack delivery times and adding each one to the series

        for(int KSIndex = 1; KSIndex<=droneforresults.KnapsackDeliveryTimes.size(); KSIndex++){
            KSSeries.getData().add(new XYChart.Data<String,Number>(Integer.toString(KSIndex), droneforresults.KnapsackDeliveryTimes.get(KSIndex-1)));
        }

        resultsBarChart.getData().addAll(FIFOSeries, KSSeries);
        resultsBarChartFrame.getChildren().add(resultsBarChart);

        // Right
        VBox resultsDataFrame = new VBox();
        VBox FIFODataFrame = new VBox();
        Text FIFOLabel = new Text("FIFO: ");
        //Put FIFODeliveryTimes.getAverage in Double.toString
        Text FIFOAverageTime = new Text("Average Time: " + Double.toString(getAverage(droneforresults.FIFODeliveryTimes)));
        //Put FIFODeliveryTimes.getWorst in Double.toString
        Text FIFOWorstTime = new Text("Worst Time: " + Double.toString(getWorst(droneforresults.FIFODeliveryTimes)));
        FIFODataFrame.getChildren().addAll(
            FIFOLabel, FIFOAverageTime, FIFOWorstTime
        );
        VBox KSDataFrame = new VBox();
        Text KSLabel = new Text("Knapsack: ");
        //Put KnapsackDeliveryTimes.getAverage in Double.toString
        Text KSAverageTime = new Text("Average Time: " + Double.toString(getAverage(droneforresults.KnapsackDeliveryTimes)));
        //Put KnapsackDeliveryTimes.getAverage in Double.toString
        Text KSWorstTime = new Text("Worst Time: " + Double.toString(getWorst(droneforresults.KnapsackDeliveryTimes)));
        KSDataFrame.getChildren().addAll(
                KSLabel, KSAverageTime, KSWorstTime
        );
        resultsDataFrame.getChildren().addAll(FIFODataFrame, new Separator(Orientation.HORIZONTAL), KSDataFrame);

        // Bottom
        HBox resultsButtonFrame = new HBox();
        Button restartBtn = new Button("Restart");
        Button saveResultsBtn = new Button("Save Results");
        Button finishBtn = new Button("Finish");
        resultsButtonFrame.getChildren().addAll(
            new ESHBox(), restartBtn, new ESHBox(), saveResultsBtn, new ESHBox(), finishBtn, new ESHBox()
        );

        // Root Assembly
        resultsContainer.getChildren().addAll(resultsBarChartFrame, resultsDataFrame);
        this.getChildren().setAll(resultsContainer, resultsButtonFrame);

        // Styles
        FIFODataFrame.setStyle(Styles.FIFODataFrame);
        FIFOLabel.setStyle(Styles.resultsDeliveryTypeLabel);
        FIFOAverageTime.setStyle(Styles.resultsDeliveryTime);
        FIFOWorstTime.setStyle(Styles.resultsDeliveryTime);
        KSLabel.setStyle(Styles.resultsDeliveryTypeLabel);
        KSAverageTime.setStyle(Styles.resultsDeliveryTime);
        KSWorstTime.setStyle(Styles.resultsDeliveryTime);
        resultsButtonFrame.setStyle(Styles.resultsButtonFrame);
        resultsDataFrame.setStyle(Styles.resultsDataFrame);

        // Growth
        VBox.setVgrow(resultsContainer, Priority.ALWAYS);
        VBox.setVgrow(resultsButtonFrame, Priority.ALWAYS);
        VBox.setVgrow(resultsBarChart, Priority.ALWAYS);
        VBox.setVgrow(resultsDataFrame, Priority.ALWAYS);

        // Dimensions
        resultsBarChart.setPrefWidth(1200);
        resultsButtonFrame.setMaxHeight(150);

        // Alignment
        resultsDataFrame.setAlignment(Pos.CENTER_LEFT);
        restartBtn.setAlignment(Pos.CENTER);
        saveResultsBtn.setAlignment(Pos.CENTER);
        finishBtn.setAlignment(Pos.CENTER);


    }
}
