package sample;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResultsPage extends VBox {

    public ResultsPage() {

        super();

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
        int FIFOIndex = 1;
        FIFOSeries.getData().addAll(
                new XYChart.Data<String, Number>(Integer.toString(FIFOIndex++), 12),
                new XYChart.Data<String, Number>(Integer.toString(FIFOIndex++), 65),
                new XYChart.Data<String, Number>(Integer.toString(FIFOIndex), 33)
        );
        XYChart.Series<String, Number> KSSeries = new XYChart.Series<>();
        KSSeries.setName("Knapsack");
        int KSIndex = 1;
        KSSeries.getData().addAll(
                new XYChart.Data<String, Number>(Integer.toString(KSIndex++), 24),
                new XYChart.Data<String, Number>(Integer.toString(KSIndex++), 15),
                new XYChart.Data<String, Number>(Integer.toString(KSIndex), 45)
        );
        resultsBarChart.getData().addAll(FIFOSeries, KSSeries);
        resultsBarChartFrame.getChildren().add(resultsBarChart);

        // Right
        VBox resultsDataFrame = new VBox();
        VBox FIFODataFrame = new VBox();
        Text FIFOLabel = new Text("FIFO: ");
        Text FIFOAverageTime = new Text("Average Time: " + Integer.toString(10));
        Text FIFOWorstTime = new Text("Worst Time: " + Integer.toString(10));
        FIFODataFrame.getChildren().addAll(
            FIFOLabel, FIFOAverageTime, FIFOWorstTime
        );
        VBox KSDataFrame = new VBox();
        Text KSLabel = new Text("Knapsack: ");
        Text KSAverageTime = new Text("Average Time: " + Integer.toString(10));
        Text KSWorstTime = new Text("Worst Time: " + Integer.toString(10));
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
