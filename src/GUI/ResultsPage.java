package GUI;

import Simulation.DataTransfer;
import Simulation.Drone;
import Simulation.Simulation;
import Simulation.DeliveryTimeList;
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



    double tempTime; //Holding variable for showing average and worst times
    DataTransfer dataTransfer = new DataTransfer(); //For bringing over data shared between classes


    //Average and worst delivery time variable, found by getting the average and worst from deliveryTimes

    //For calculating the average delivery time of a group of times
    public static Double getAverage(ArrayList<ArrayList<Double>> times){
        double sum = 0; //sum of all the times
        int numberOfTimes = 0; //number of times in the list of times

        //for every list of times inside of the list of times, add each element to the sum
        for(int i = 0; i<times.size(); i++){
            //Build up the number of delivery times at every iteration
            numberOfTimes+=times.get(i).size();
            for(int j = 0; j<times.get(i).size(); j++){
                sum += times.get(i).get(j);
            }
        }
        //Return average
        return sum/numberOfTimes;
    }
    public static Double getWorst(ArrayList<ArrayList<Double>> times){
        double worst = 0; //Stores biggest time in times

        for(int i = 0; i<times.size(); i++){
            for(int j = 0; j<times.get(i).size(); j++){
                //If the time found is greater than the worst time, make it the worst time
                if(times.get(i).get(j) > worst){
                    worst = times.get(i).get(j);
                }
            }
        }
        //Return that worst time
        return worst;
    }

    //Methods for saving to a file
    private String getResultsStr() {
        String ret = "Your Results:\n";
        ret += "FIFO Avg Time: " + Values.simulation.FIFOaverageTime.toString() + "\n";
        ret += "FIFO Worst Time: " + Values.simulation.FIFOworstTime.toString() + "\n\n";
        ret += "KS Avg Time: " + Values.simulation.KSaverageTime.toString() + "\n";
        ret += "KS Worst Time: " + Values.simulation.KSworstTime.toString() + "\n\n";

        // Need some way to get shift information
        //DataTransfer.getNumShifts()  DataTransfer.getShifts()

        return ret;
    }
    // writes string str to file file
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

        //If there is a recent simulation show the results for that simulation
        //If no recent simulation, show a blank results page
        if (Values.simulation != null) {

            //Get Simulation and Drone for results
            Simulation recentSimulation = Values.simulation;
            Drone droneforresults = recentSimulation.drone;
            DeliveryTimeList FIFODTL = droneforresults.getFIFODeliveryTimesList();
            DeliveryTimeList KSDTL = droneforresults.getKnapsackDeliveryTimesList();

            //Start creating graphs

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
            int[] hourCounter = new int[dataTransfer.getNumShifts()*dataTransfer.getNumSimulations()];
            //int[] hourCounter = new int[recentSimulation.getNumShifts()];
            //dataTransfer.getNumShifts * dataTransfer.getNumSimulations?

            //Initalize hourCounter
            for (int i = 0; i < hourCounter.length; i++) {
                hourCounter[i] = 0;
            }

            //For loop iterating over the FIFO delivery times
            for (int FIFOIndex = 0; FIFOIndex < FIFODTL.getDeliveryTimesList().size(); FIFOIndex++) {
                //Increment hour counter based on what part of the FIFO list the times are in
                for(int i = 0; i<FIFODTL.getDeliveryTimesList().get(FIFOIndex).size(); i++){
                    hourCounter[FIFOIndex%dataTransfer.getNumShifts()]++;
                }
            }

            //For loop adding the hour counter times to the bar graph
            for (int i = 0; i < dataTransfer.getNumShifts(); i++) {
                FIFOSeries.getData().add(new XYChart.Data<String, Number>(Integer.toString(i + 1), hourCounter[i]/dataTransfer.getNumSimulations()));
            }

            XYChart.Series<String, Number> KSSeries = new XYChart.Series<>();
            KSSeries.setName("Knapsack");

            //Reset hour counter
            for (int i = 0; i < hourCounter.length; i++) {
                hourCounter[i] = 0;
            }

            //For loop iterating over the Knapsack delivery times
            for (int KSIndex = 0; KSIndex < KSDTL.getDeliveryTimesList().size(); KSIndex++) {
                //Increment hour counter based on what part of the knapsack list the times are in
                for(int i = 0; i<KSDTL.getDeliveryTimesList().get(KSIndex).size(); i++){
                    hourCounter[KSIndex%dataTransfer.getNumShifts()]++;
                }
            }

            //For loop adding the hour counter times to the bar graph
            for (int i = 0; i < dataTransfer.getNumShifts(); i++) {
                KSSeries.getData().add(new XYChart.Data<String, Number>(Integer.toString(i + 1), hourCounter[i]/dataTransfer.getNumSimulations()));
            }

            //Add the series of data to the bar graph
            resultsBarChart.getData().addAll(FIFOSeries, KSSeries);
            //Show graph
            resultsBarChartFrame.getChildren().add(resultsBarChart);

            // Right
            VBox resultsDataFrame = new VBox();

            //Create FIFO display
            VBox FIFODataFrame = new VBox();
            Text FIFOLabel = new Text("FIFO: ");

            //Calculate results times and put in simulation
            recentSimulation.FIFOaverageTime = getAverage(FIFODTL.getDeliveryTimesList());
            recentSimulation.FIFOworstTime = getWorst(FIFODTL.getDeliveryTimesList());
            recentSimulation.KSaverageTime = getAverage(KSDTL.getDeliveryTimesList());
            recentSimulation.KSworstTime = getWorst(KSDTL.getDeliveryTimesList());

            //Calculate FIFO average time and display it
            tempTime = Math.round(recentSimulation.FIFOaverageTime*100)/100.0;
            Text FIFOAverageTime = new Text("Average Time: " + tempTime + " minutes");
            //Calculate worst FIFO time and display it
          
            tempTime = Math.round(recentSimulation.FIFOworstTime*100)/100.0;
            Text FIFOWorstTime = new Text("Worst Time: " + tempTime + " minutes");

            //Add to display
            FIFODataFrame.getChildren().addAll(
                    FIFOLabel, FIFOAverageTime, FIFOWorstTime
            );

            //Create knapsack display
            VBox KSDataFrame = new VBox();
            Text KSLabel = new Text("Knapsack: ");

            //Calculate knapsack average time and display it
            tempTime = Math.round(recentSimulation.KSaverageTime * 100)/100.0;
            Text KSAverageTime = new Text("Average Time: " + tempTime +" minutes");
            //Calculate knapsack worst time and display it

            tempTime = Math.round(recentSimulation.KSworstTime * 100)/100.0;
            Text KSWorstTime = new Text("Worst Time: " + tempTime +" minutes");

            //Add times to display
            KSDataFrame.getChildren().addAll(
                    KSLabel, KSAverageTime, KSWorstTime
            );

            //Show display on page
            resultsDataFrame.getChildren().addAll(FIFODataFrame, new Separator(Orientation.HORIZONTAL), KSDataFrame);

            // Bottom
            HBox resultsButtonFrame = new HBox();

            //Create buttons on results page
            Button restartBtn = new Button("Restart");
            Button saveResultsBtn = new Button("Save Results");
            Button finishBtn = new Button("Finish");

            //Show buttons on page
            resultsButtonFrame.getChildren().addAll(
                    new ESHBox(), restartBtn, new ESHBox(), saveResultsBtn, new ESHBox(), finishBtn, new ESHBox()
            );

            //Create functionality for clicking restart button
            restartBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //Run new simulation
                    Values.simulation = new Simulation();
                    Values.simulation.runSimulation();

                    //Go to main menu
                    Values.primaryStage.getScene().setRoot(Values.rootPage);
                }
            });

            //Create functionality for finish button
            finishBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //Go to main menu
                    Values.primaryStage.getScene().setRoot(Values.rootPage);
                }
            });

            // Added by Josh - Save Button Functionality in results page
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

            // Root Assembly (Show everything on the page)
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
            /*
            Blank results page throughout this else statement
            No separate formatting for it from the actual results page

            Added default blank values instead of calling actual values
            (Will have null pointer exception if I call actual values)
             */
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

            //Initialize blank variables
            int[] hourCounter = new int[4];
            for (int i = 0; i < hourCounter.length; i++) {
                hourCounter[i] = 0;
            }

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

            Text FIFOAverageTime = new Text("Average Time: " + Double.toString(0));
            Text FIFOWorstTime = new Text("Worst Time: " + Double.toString(0));
            FIFODataFrame.getChildren().addAll(
                    FIFOLabel, FIFOAverageTime, FIFOWorstTime
            );
            VBox KSDataFrame = new VBox();
            Text KSLabel = new Text("Knapsack: ");
            Text KSAverageTime = new Text("Average Time: " + Double.toString(0));
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
