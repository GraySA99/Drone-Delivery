package sample;

import Mapping.Waypoint;
import Simulation.DataTransfer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShiftsPage extends BorderPane {

    private ListView<VBox> hoursList;
    private TextField shiftHoursEnt, numSimsEnt;

    public ShiftsPage() {

        super();

        PageTitle pageTitle = new PageTitle("Shifts");

        // Right Side
        StackPane hoursListContainer = new StackPane();
        hoursList = new ListView<>();
        hoursList.setPrefWidth(550);
        hoursListContainer.getChildren().add(hoursList);

        // Left Side
        VBox entryContainer = new VBox();
        Text shiftHoursLabel = new Text("Shift Hours");
        shiftHoursEnt = new TextField();
        Text numSimsLabel = new Text("Number of Simulations");
        numSimsEnt = new TextField();
        shiftHoursEnt.setText("4");

        shiftHoursEnt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                try {
                    int i = Integer.parseInt(t1);
                    int j = Integer.parseInt(s);

                    reload(i, j);
                } catch (NumberFormatException e) {}
            }
        });

        entryContainer.getChildren().addAll(
                shiftHoursLabel,
                shiftHoursEnt,
                numSimsLabel,
                numSimsEnt);

        this.setRight(hoursListContainer);
        this.setLeft(entryContainer);
        this.setTop(pageTitle);

        initFromFile();
    }

    public void reload(int newNumHours, int oldNumHours) {

        if (newNumHours < oldNumHours) {

            for (int i = newNumHours+1; i <= oldNumHours; ++i) {

                DataTransfer.removeShift(i);
            }
        } else if (newNumHours > oldNumHours) {

            for (int i = oldNumHours + 1; i <= newNumHours; ++i) {

                DataTransfer.addShift(i, 0);
            }
        }

        if (newNumHours < hoursList.getItems().size()) {

            int i = hoursList.getItems().size();
            while (i > newNumHours) {

                hoursList.getItems().remove(hoursList.getItems().size()-1);
                i--;
            }

        } else {

            for (int i = hoursList.getItems().size(); i < newNumHours; ++i) {

                VBox temp = new VBox();
                Text hourLabel = new Text("Hour " + (i+1));
                TextField hourNum = new TextField();
                hourNum.setPromptText("Number of orders in hour " + (i+1));
                if (DataTransfer.getShift(i+1) != null)
                    hourNum.setText(Integer.toString(DataTransfer.getShift(i+1)));
                temp.getChildren().addAll(hourLabel, hourNum);

                hourNum.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                        int shiftHour =
                                Integer.parseInt(((Text)((VBox)hourNum.getParent()).getChildren().get(0))
                                        .getText().replace("Hour ", "").strip());
                        System.out.println(shiftHour);
                        try {
                            int oldVal = Integer.parseInt(s);
                            int newVal = Integer.parseInt(t1);


                            DataTransfer.addShift(shiftHour, newVal);

                        } catch (NumberFormatException e) {
                            DataTransfer.addShift(shiftHour, 0);
                        }
                    }
                });

                hoursList.getItems().add(temp);
            }
        }
    }

    private void initFromFile() {

        try {

            FileInputStream fis = new FileInputStream(Values.defaultShiftsFileName);
            Scanner fileIn = new Scanner(fis);
            if (!fileIn.hasNextLine()) { return; }
            String fileLine = fileIn.nextLine();

            while (fileIn.hasNextLine() && !fileLine.equals("@Shifts")) { fileLine = fileIn.nextLine(); }
            if (!fileIn.hasNextLine()) { return; }

            fileLine = fileIn.nextLine();
            hoursList.getItems().clear();
            while (fileIn.hasNextLine()) {

                if (fileLine.strip().equals("@/Shifts")) { break; }

                if (fileLine.contains("*")) {

                    fileLine = fileLine.replace("*", "").strip();
                    int shiftNum = Integer.parseInt(fileLine.split("&")[0]);
                    int numOrders = Integer.parseInt(fileLine.split("&")[1]);

                    VBox temp = new VBox();
                    Text hourLabel = new Text("Hour " + shiftNum);
                    TextField hourNum = new TextField();
                    hourNum.setPromptText("Number of orders in hour " + shiftNum);
                    hourNum.setText(Integer.toString(numOrders));
                    temp.getChildren().addAll(hourLabel, hourNum);

                    hoursList.getItems().add(temp);

                    hourNum.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                            int shiftHour =
                                    Integer.parseInt(((Text)((VBox)hourNum.getParent()).getChildren().get(0))
                                            .getText().replace("Hour ", "").strip());
                            System.out.println(shiftHour);
                            try {
                                int oldVal = Integer.parseInt(s);
                                int newVal = Integer.parseInt(t1);


                                DataTransfer.addShift(shiftHour, newVal);

                            } catch (NumberFormatException e) {
                                DataTransfer.addShift(shiftHour, 0);
                            }
                        }
                    });

                    DataTransfer.addShift(shiftNum, numOrders);

                } else {

                    String numShifts = fileLine.strip().split("&")[0];
                    String numSims = fileLine.strip().split("&")[1];
                    shiftHoursEnt.setText(numShifts);
                    numSimsEnt.setText(numSims);

                    DataTransfer.setNumSimulations(Integer.parseInt(numSims));
                }

                fileLine = fileIn.nextLine();
            }
        } catch (FileNotFoundException e) {

            System.out.println("Problem With File");
            e.printStackTrace();
        }
    }

    public void resizeWindow() {

        this.setMinWidth(Values.windowWidth * Values.mainPageWidthPercent);
    }
}
