package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ShiftsPage extends BorderPane {

    private ListView<VBox> hoursList;

    public ShiftsPage() {

        super();

        PageTitle pageTitle = new PageTitle("Shifts");

        // Left Side
        StackPane hoursListContainer = new StackPane();
        hoursList = new ListView<>();
        hoursList.setPrefWidth(550);
        hoursListContainer.setStyle(Styles.shiftsListContainer);
        hoursList.setStyle(Styles.shiftsList);
        hoursListContainer.getChildren().add(hoursList);

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

        this.setLeft(hoursListContainer);
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
