package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ShiftsPage extends BorderPane {

    private ListView<VBox> hoursList;

    public ShiftsPage() {

        super();

        Text pageTitleLabel = new Text("Shifts");
        HBox pageTitle = new HBox();
        pageTitle.setStyle(Styles.pageTitle);
        HBox pageTitleLabelContainer = new HBox();
        HBox pageTitleES1 = new HBox();
        HBox pageTitleES2 = new HBox();
        HBox.setHgrow(pageTitleES1, Priority.ALWAYS);
        HBox.setHgrow(pageTitleES2, Priority.ALWAYS);
        HBox.setHgrow(pageTitle, Priority.ALWAYS);
        pageTitleLabelContainer.getChildren().add(pageTitleLabel);
        pageTitle.getChildren().addAll(pageTitleES1, pageTitleLabelContainer, pageTitleES2);
        pageTitleLabel.setStyle(Styles.pageTitleLabel);
        pageTitleLabelContainer.setStyle(Styles.pageTitleLabelContainer);

        // Left Side
        hoursList = new ListView<>();
        hoursList.setPrefWidth(550);
        hoursList.setStyle(Styles.shiftsList);

        // Right Side
        VBox entryContainer = new VBox();
        Text shiftHoursLabel = new Text("Shift Hours");
        TextField shiftHoursEnt = new TextField();
        Text numSimsLabel = new Text("Number of Simulations");
        TextField numSimsEnt = new TextField();
        shiftHoursEnt.setText("4");

        shiftHoursEnt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                try {
                    int i = Integer.parseInt(t1);

                    reload(i);
                } catch (NumberFormatException e) {}
            }
        });

        entryContainer.getChildren().addAll(
                shiftHoursLabel,
                shiftHoursEnt,
                numSimsLabel,
                numSimsEnt);

        try {

            reload(Integer.parseInt(shiftHoursEnt.getText()));
        } catch (NumberFormatException e) {

            System.out.println("Invalid Number Format for Shifts Page");
        }

        this.setLeft(hoursList);
        this.setRight(entryContainer);
        this.setTop(pageTitle);
        this.setStyle(Styles.shiftsPage);
    }

    public void reload(int numHours) {

        if (numHours < hoursList.getItems().size()) {

            int i = hoursList.getItems().size();
            while (i > numHours) {

                System.out.println(hoursList.getItems().size() -1);
                hoursList.getItems().remove(hoursList.getItems().size()-1);
                i--;
            }

        } else {

            for (int i = hoursList.getItems().size(); i < numHours; ++i) {

                VBox temp = new VBox();
                Text hourLabel = new Text("Hour " + (i+1));
                TextField hourNum = new TextField();
                hourNum.setPromptText("Number of orders in hour " + (i+1));
                temp.getChildren().addAll(hourLabel, hourNum);

                hoursList.getItems().add(temp);
            }
        }
    }
}
