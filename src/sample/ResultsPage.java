package sample;

import Simulation.Drone;
import Simulation.Simulation;
import Simulation.DataTransfer;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ResultsPage extends VBox {


    double tempTime;
    DataTransfer dataTransfer = new DataTransfer();


    //Average and worst delivery time variable, found by getting the average and worst from deliveryTimes
    //public Double average = getAverage(drone.deliveryTimes);
    //public Double worst = getWorst(drone.deliveryTimes);

    public static Double getAverage(ArrayList<ArrayList<Double>> times){
        double sum = 0;
        int numberOfTimes = 0;
        for(int i = 0; i<times.size(); i++){
            numberOfTimes+=times.get(i).size();
            for(int j = 0; j<times.get(i).size(); j++){
                sum += times.get(i).get(j);
            }
        }
        return sum/numberOfTimes;
    }
    public static Double getWorst(ArrayList<ArrayList<Double>> times){
        double worst = 0;
        for(int i = 0; i<times.size(); i++){
            for(int j = 0; j<times.get(i).size(); j++){
                if(times.get(i).get(j) > worst){
                    worst = times.get(i).get(j);
                }
            }
        }
        return worst;
    }

    //Methods for saving to a file
    private String getResultsStr() {
        String ret = "Your Results:\n";
        ret += "FIFO Avg Time: " + Values.simulation.FIFOaverageTime.toString() + "\n";
        ret += "FIFO Worst Time: " + Values.simulation.FIFOworstTime.toString() + "\n\n";
        ret += "KS Avg Time:" + Values.simulation.KSaverageTime.toString() + "\n";
        ret += "KS Worst Time: " + Values.simulation.KSworstTime.toString() + "\n\n";

        // Need some way to get shift information
        //DataTransfer.getNumShifts()  DataTransfer.getShifts()

        return ret;
    }
    private void writeTextToFile(String str, File file)
    {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(str);
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ResultsPage() {

        super();

        //Figure out how to get the most recent of each drone and bring them into this page
        if (Values.simulation != null) {
            Simulation recentSimulation = Values.simulation;
            Drone droneforresults = recentSimulation.drone;

            HBox resultsContainer = new HBox();

            // Left
            VBox resultsBarChartFrame = new VBox();
            CategoryAxis resultsXAxis = new CategoryAxis();
            resultsXAxis.setLabel("Hours");
            NumberAxis resultsYAxis = new NumberAxis();
            resultsYAxis.setLabel("Number of Orders Delivered");
            BarChart<String, Number> resultsBarChart = new BarChart<String, Number>(resultsXAxis, resultsYAxis);
            resultsBarChart.setTitle("Results");
            XYChart.Series<String, Number> FIFOSeries = new XYChart.Series<>();
            FIFOSeries.setName("FIFO");

            //Hour Counter for For Loops
            int[] hourCounter = new int[dataTransfer.getNumShifts()];
            for (int i = 0; i < hourCounter.length; i++) {
                hourCounter[i] = 0;
            }

            //For loop iterating over the FIFO delivery times and adding each of them to the hour counter
            for (int FIFOIndex = 0; FIFOIndex < droneforresults.FIFODeliveryTimes.size(); FIFOIndex++) {
                for(int i = 0; i<droneforresults.FIFODeliveryTimes.get(FIFOIndex).size(); i++){
                    hourCounter[FIFOIndex]++;
                }
            }

            //For loop adding the hour counter to the graph
            for (int i = 0; i < hourCounter.length; i++) {
                FIFOSeries.getData().add(new XYChart.Data<String, Number>(Integer.toString(i + 1), hourCounter[i]));
            }

            XYChart.Series<String, Number> KSSeries = new XYChart.Series<>();
            KSSeries.setName("Knapsack");

            //Reset Variables
            for (int i = 0; i < hourCounter.length; i++) {
                hourCounter[i] = 0;
            }

            //For loop iterating over the Knapsack delivery times and adding each of them to the hour counter
            for (int KSIndex = 0; KSIndex < droneforresults.KnapsackDeliveryTimes.size(); KSIndex++) {
                for(int i = 0; i<droneforresults.KnapsackDeliveryTimes.get(KSIndex).size(); i++){
                    hourCounter[KSIndex]++;
                }
            }

            //For loop adding the hour counter to the graph
            for (int i = 0; i < hourCounter.length; i++) {
                KSSeries.getData().add(new XYChart.Data<String, Number>(Integer.toString(i + 1), hourCounter[i]));
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
            tempTime = Math.round(recentSimulation.FIFOaverageTime*100)/100.0;
            Text FIFOAverageTime = new Text("Average Time: " + tempTime + " minutes");
            //Put FIFODeliveryTimes.getWorst in Double.toString
            tempTime = Math.round(recentSimulation.FIFOworstTime*100)/100.0;
            Text FIFOWorstTime = new Text("Worst Time: " + tempTime + " minutes");
            FIFODataFrame.getChildren().addAll(
                    FIFOLabel, FIFOAverageTime, FIFOWorstTime
            );
            VBox KSDataFrame = new VBox();
            Text KSLabel = new Text("Knapsack: ");
            //Put KnapsackDeliveryTimes.getAverage in Double.toString
            tempTime = Math.round(recentSimulation.KSaverageTime * 100)/100.0;
            Text KSAverageTime = new Text("Average Time: " + tempTime +" minutes");
            //Put KnapsackDeliveryTimes.getAverage in Double.toString
            tempTime = Math.round(recentSimulation.KSworstTime * 100)/100.0;
            Text KSWorstTime = new Text("Worst Time: " + tempTime +" minutes");
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

            restartBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Values.simulation = new Simulation();
                    Values.simulation.runSimulation();
                    Values.primaryStage.getScene().setRoot(Values.rootPage);
                }
            });

            finishBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {

                    Values.primaryStage.getScene().setRoot(Values.rootPage);
                }
            });

            // Added by Josh - Save Button Functionality
            saveResultsBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();

                    //If we want we can set extension filters for text files
                    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                    fileChooser.getExtensionFilters().add(filter);

                    //Show save file dialogue
                    File file = fileChooser.showSaveDialog(Values.primaryStage);

                    if(file != null) {
                        writeTextToFile(getResultsStr(), file);
                    }
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
        } else {
            HBox resultsContainer = new HBox();

            // Left
            VBox resultsBarChartFrame = new VBox();
            CategoryAxis resultsXAxis = new CategoryAxis();
            resultsXAxis.setLabel("Hours");
            NumberAxis resultsYAxis = new NumberAxis();
            resultsYAxis.setLabel("Number of Orders Delivered");
            BarChart<String, Number> resultsBarChart = new BarChart<String, Number>(resultsXAxis, resultsYAxis);
            resultsBarChart.setTitle("Results");
            XYChart.Series<String, Number> FIFOSeries = new XYChart.Series<>();
            FIFOSeries.setName("FIFO");

            //Hour Counter for For Loops
            int[] hourCounter = new int[4];
            for (int i = 0; i < hourCounter.length; i++) {
                hourCounter[i] = 0;
            }

            //For loop adding the hour counter to the graph
            for (int i = 0; i < hourCounter.length; i++) {
                FIFOSeries.getData().add(new XYChart.Data<String, Number>(Integer.toString(i + 1), hourCounter[i]));
            }

            XYChart.Series<String, Number> KSSeries = new XYChart.Series<>();
            KSSeries.setName("Knapsack");

            //Reset Variables
            for (int i = 0; i < hourCounter.length; i++) {
                hourCounter[i] = 0;
            }

            //For loop adding the hour counter to the graph
            for (int i = 0; i < hourCounter.length; i++) {
                KSSeries.getData().add(new XYChart.Data<String, Number>(Integer.toString(i + 1), hourCounter[i]));
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
                @Override
                public void handle(ActionEvent e) {

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
