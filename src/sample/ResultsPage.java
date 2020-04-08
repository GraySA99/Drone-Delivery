package sample;

import Simulation.Drone;
import Simulation.Simulation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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





    //Average and worst delivery time variable, found by getting the average and worst from deliveryTimes
    //public Double average = getAverage(drone.deliveryTimes);
    //public Double worst = getWorst(drone.deliveryTimes);

    public static Double getAverage(ArrayList<Double> times){
        double sum = 0;
        int negativeCounter = 0;
        for(int i = 0; i<times.size(); i++){
            if(times.get(i) == -1){
                negativeCounter++;
            }else {
                sum += times.get(i);
            }
        }
        return sum/(times.size() - negativeCounter);
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

        //Figure out how to get the most recent of each drone and bring them into this page
        if(Values.simulation != null){
            Simulation recentSimulation = Values.simulation;
            Drone droneforresults = recentSimulation.drone;

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

            //Hour Counter for For Loops
            int currentHour = 0;
            int [] hourCounter = new int [recentSimulation.getNumShifts()];
            for(int i = 0; i<hourCounter.length; i++){
                hourCounter[i] = 0;
            }

            //For loop iterating over the FIFO delivery times and adding each of them to the hour counter
            for(int FIFOIndex = 0; FIFOIndex<droneforresults.FIFODeliveryTimes.size(); FIFOIndex++){
                if(droneforresults.FIFODeliveryTimes.get(FIFOIndex) == -1){
                    currentHour++;
                }else {
                    hourCounter[currentHour]++;
                }
            }

            //For loop adding the hour counter to the graph
            for(int i = 0; i<hourCounter.length; i++){
                FIFOSeries.getData().add(new XYChart.Data<String,Number>(Integer.toString(i+1), hourCounter[i]));
            }

            XYChart.Series<String, Number> KSSeries = new XYChart.Series<>();
            KSSeries.setName("Knapsack");

            //Reset Variables
            currentHour = 0;
            for(int i = 0; i<hourCounter.length; i++){
                hourCounter[i] = 0;
            }

            //For loop iterating over the Knapsack delivery times and adding each of them to the hour counter
            for(int KSIndex = 0; KSIndex<droneforresults.KnapsackDeliveryTimes.size(); KSIndex++){
                if(droneforresults.KnapsackDeliveryTimes.get(KSIndex) == -1){
                    currentHour++;
                }else {
                    hourCounter[currentHour]++;
                }
            }

            //For loop adding the hour counter to the graph
            for(int i = 0; i<hourCounter.length; i++){
                KSSeries.getData().add(new XYChart.Data<String,Number>(Integer.toString(i+1), hourCounter[i]));
            }

            resultsBarChart.getData().addAll(FIFOSeries, KSSeries);
            resultsBarChartFrame.getChildren().add(resultsBarChart);

            // Right
            VBox resultsDataFrame = new VBox();
            VBox FIFODataFrame = new VBox();
            Text FIFOLabel = new Text("FIFO: ");

            //Calculate results times and put in simulation
            recentSimulation.FIFOaverageTime = getAverage(droneforresults.FIFODeliveryTimes);
            recentSimulation.FIFOworstTime = getWorst(droneforresults.FIFODeliveryTimes);
            recentSimulation.KSaverageTime = getAverage(droneforresults.KnapsackDeliveryTimes);
            recentSimulation.KSworstTime = getWorst(droneforresults.KnapsackDeliveryTimes);

            //Put FIFODeliveryTimes.getAverage in Double.toString
            Text FIFOAverageTime = new Text("Average Time: " + Double.toString(recentSimulation.FIFOaverageTime));
            //Put FIFODeliveryTimes.getWorst in Double.toString
            Text FIFOWorstTime = new Text("Worst Time: " + Double.toString(recentSimulation.FIFOworstTime));
            FIFODataFrame.getChildren().addAll(
                    FIFOLabel, FIFOAverageTime, FIFOWorstTime
            );
            VBox KSDataFrame = new VBox();
            Text KSLabel = new Text("Knapsack: ");
            //Put KnapsackDeliveryTimes.getAverage in Double.toString
            Text KSAverageTime = new Text("Average Time: " + Double.toString(recentSimulation.KSaverageTime));
            //Put KnapsackDeliveryTimes.getAverage in Double.toString
            Text KSWorstTime = new Text("Worst Time: " + Double.toString(recentSimulation.KSworstTime));
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

            finishBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {

                    Values.primaryStage.getScene().setRoot(Values.rootPage);
                }
            });

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
        }else {
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

            //Hour Counter for For Loops
            int [] hourCounter = new int [4];
            for(int i = 0; i<hourCounter.length; i++){
                hourCounter[i] = 0;
            }

            //For loop adding the hour counter to the graph
            for(int i = 0; i<hourCounter.length; i++){
                FIFOSeries.getData().add(new XYChart.Data<String,Number>(Integer.toString(i+1), hourCounter[i]));
            }

            XYChart.Series<String, Number> KSSeries = new XYChart.Series<>();
            KSSeries.setName("Knapsack");

            //Reset Variables
            for(int i = 0; i<hourCounter.length; i++){
                hourCounter[i] = 0;
            }

            //For loop adding the hour counter to the graph
            for(int i = 0; i<hourCounter.length; i++){
                KSSeries.getData().add(new XYChart.Data<String,Number>(Integer.toString(i+1), hourCounter[i]));
            }

            resultsBarChart.getData().addAll(FIFOSeries, KSSeries);
            resultsBarChartFrame.getChildren().add(resultsBarChart);

            // Right
            VBox resultsDataFrame = new VBox();
            VBox FIFODataFrame = new VBox();
            Text FIFOLabel = new Text("FIFO: ");

            //Put FIFODeliveryTimes.getAverage in Double.toString
            Text FIFOAverageTime = new Text("Average Time: " + Double.toString(0));
            //Put FIFODeliveryTimes.getWorst in Double.toString
            Text FIFOWorstTime = new Text("Worst Time: " + Double.toString(0));
            FIFODataFrame.getChildren().addAll(
                    FIFOLabel, FIFOAverageTime, FIFOWorstTime
            );
            VBox KSDataFrame = new VBox();
            Text KSLabel = new Text("Knapsack: ");
            //Put KnapsackDeliveryTimes.getAverage in Double.toString
            Text KSAverageTime = new Text("Average Time: " + Double.toString(0));
            //Put KnapsackDeliveryTimes.getAverage in Double.toString
            Text KSWorstTime = new Text("Worst Time: " + Double.toString(0));
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

            finishBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {

                    Values.primaryStage.getScene().setRoot(Values.rootPage);
                }
            });

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
}
