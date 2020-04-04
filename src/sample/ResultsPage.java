package sample;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResultsPage extends BorderPane {

    public ResultsPage() {

        super();

        // Top
        PageTitle pageTitle = new PageTitle("Results");

        // Left

        // Right

        // Bottom
        BorderPane bottomFrame = new BorderPane();
        bottomFrame.setStyle(Styles.resultsBottomFrame);

        // Sub Top
        HBox bottomSubTopFrame = new HBox();
        Text averageTimeFIFOText = new Text("Average Time: ");
        Text averageTimeKSText = new Text("Average Time: ");
        bottomSubTopFrame.getChildren().addAll(
            averageTimeFIFOText,
            getEmptySpace(),
            averageTimeKSText,
            getEmptySpace()
        );

        // Sub Center
        HBox bottomSubCenterFrame = new HBox();
        Text worstTimeFIFOText = new Text("Worst Time: ");
        Text worstTimeKSText = new Text("Worst Time: ");
        bottomSubCenterFrame.getChildren().addAll(
                worstTimeFIFOText,
                getEmptySpace(),
                worstTimeKSText,
                getEmptySpace()
        );

        // Sub Bottom
        HBox bottomSubBottomFrame = new HBox();
        Button restartBtn = new Button("Restart");
        Button saveResultsBtn = new Button("Save Results");
        Button finishBtn = new Button("Finish");
        bottomSubBottomFrame.getChildren().addAll(
                getEmptySpace(),
                restartBtn,
                getEmptySpace(),
                saveResultsBtn,
                getEmptySpace(),
                finishBtn,
                getEmptySpace()
        );
        bottomFrame.setTop(bottomSubTopFrame);
        bottomFrame.setCenter(bottomSubCenterFrame);
        bottomFrame.setBottom(bottomSubBottomFrame);




        this.setTop(pageTitle);
        this.setBottom(bottomFrame);
    }

    private HBox getEmptySpace() {

        HBox temp = new HBox();
        HBox.setHgrow(temp, Priority.ALWAYS);
        return temp;
    }
}
