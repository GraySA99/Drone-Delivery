package sample;

import Food.Food;

import Mapping.Waypoint;
import Simulation.DataTransfer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class FoodPage extends BorderPane {

    private ListView<HBox> foodList;
    private TextField foodNameEnt;
    private TextField foodWeightEnt;

    public FoodPage() {

        super(); // Super Constructor
        this.setStyle(Styles.foodPage);

        PageTitle pageTitle = new PageTitle("Food Items");

        // Right Side - The List of Entered Food
        StackPane foodListContainer = new StackPane();
        foodListContainer.setStyle(Styles.foodListContainer);
        foodList = new ListView<HBox>();
        foodList.setStyle(Styles.foodList);
        foodList.getItems().add(new HBox());
        foodListContainer.getChildren().add(foodList);

        // Left Side - How the user enters the food
        GridPane foodItemEntry = new GridPane(); // Container for right side
        Text foodNameLabel = new Text("Name");
        Text foodWeightLabel = new Text("Weight");
        foodNameEnt = new TextField();
        foodNameEnt.setPromptText("ex. Burger");
        foodWeightEnt = new TextField();
        foodWeightEnt.setPromptText("ex. 10");
        Button foodAddItemBtn = new Button("Add");
        Button foodRemoveItemBtn = new Button("Remove");

        // Add food item into List on Left Side
        foodAddItemBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = foodNameEnt.getText();
                String weight = foodWeightEnt.getText();

                if (!name.strip().isBlank() && !weight.strip().isBlank()
                        && isNumeric(weight)
                        && DataTransfer.getFood(name) == null) {

                    DataTransfer.addFood(new Food(name, Double.parseDouble(weight)));
                }

                HBox foodItemFrame = new HBox();
                Text foodItemName = new Text(name);
                Text foodItemWeight = new Text(weight + " oz.");
                foodItemFrame.getChildren().addAll(foodItemName, new ESHBox(), foodItemWeight);
                foodItemFrame.setOnMouseClicked(evt -> {

                    String foodName = ((Text)foodItemFrame.getChildren().get(0)).getText();
                    String foodWeight = ((Text)foodItemFrame.getChildren().get(2)).getText()
                            .replaceAll(" oz.", "");

                    foodNameEnt.setText(foodName);
                    foodWeightEnt.setText(foodWeight);
                });

                if (foodList.getItems().size() == 1 && foodList.getItems().get(0).getChildren().isEmpty()) {
                    foodList.getItems().clear();
                }
                foodList.getItems().add(foodItemFrame);

                foodNameEnt.clear();
                foodWeightEnt.clear();
            }
        });

        // Remove food item from list on left side if it exists
        foodRemoveItemBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = foodNameEnt.getText();

                if (DataTransfer.getMeal(name) != null) {

                    DataTransfer.removeFood(DataTransfer.getFood(name));
                    foodList.getItems().removeIf(hb -> ((Text) hb.getChildren().get(0)).getText().equals(name));

                    if (foodList.getItems().isEmpty())
                        foodList.getItems().add(new HBox());

                    foodNameEnt.clear();
                    foodWeightEnt.clear();
                }
            }
        });

        // Add Items to Grid
        foodItemEntry.add(foodNameLabel, 0, 0, 2, 1);
        foodItemEntry.add(foodNameEnt, 0, 1, 2, 1);
        foodItemEntry.add(foodWeightLabel, 0, 3, 2, 1);
        foodItemEntry.add(foodWeightEnt, 0, 4, 2, 1);
        foodItemEntry.add(foodAddItemBtn, 0, 6, 1, 1);
        foodItemEntry.add(foodRemoveItemBtn, 1, 6, 1, 1);

        // Set Styles and Sizes
        foodItemEntry.setStyle(Styles.foodItemEntry);
        foodNameLabel.setStyle(Styles.foodLabels);
        foodWeightLabel.setStyle(Styles.foodLabels);
        foodNameEnt.setPrefSize(50, 10);
        foodWeightEnt.setPrefSize(50, 10);
        foodNameEnt.setStyle(Styles.foodEntries);
        foodWeightEnt.setStyle(Styles.foodEntries);

        // Add Frames to this
        this.setLeft(foodItemEntry);
        this.setRight(foodListContainer);
        this.setTop(pageTitle);

        initFromFile();
    }

    // Function: isNumeric
    // Purpose: checks if given string is a number
    // Input: String s
    // Returns: boolean
    private boolean isNumeric(String s) {

        try {

            Double.parseDouble(s);
            return true;

        } catch (NumberFormatException e) {

            return false;
        }
    }

    private void initFromFile() {

        try {

            FileInputStream fis = new FileInputStream(Values.defaultFoodFileName);
            Scanner fileIn = new Scanner(fis);
            if (!fileIn.hasNextLine()) { return; }
            String fileLine = fileIn.nextLine();

            while (fileIn.hasNextLine() && !fileLine.equals("@Food")) { fileLine = fileIn.nextLine(); }
            if (!fileIn.hasNextLine()) { return; }

            fileLine = fileIn.nextLine();
            foodList.getItems().clear();
            while (fileIn.hasNextLine()) {

                if (fileLine.strip().equals("@/Food")) { break; }

                String name = fileLine.strip().split("&")[0];
                String weight = fileLine.strip().split("&")[1];

                HBox foodItemFrame = new HBox();
                Text foodItemName = new Text(name);
                Text foodItemWeight = new Text(weight + " oz.");
                foodItemFrame.getChildren().addAll(foodItemName, new ESHBox(), foodItemWeight);
                foodItemFrame.setOnMouseClicked(evt -> {

                    String foodName = ((Text)foodItemFrame.getChildren().get(0)).getText();
                    String foodWeight = ((Text)foodItemFrame.getChildren().get(2)).getText()
                            .replaceAll(" oz.", "");

                    foodNameEnt.setText(foodName);
                    foodWeightEnt.setText(foodWeight);
                });

                foodList.getItems().add(foodItemFrame);

                DataTransfer.addFood(new Food(name, Double.parseDouble(weight)));

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
