package sample;

import Food.Food;

import Simulation.DataTransfer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Set;

public class FoodPage extends BorderPane {

    public FoodPage() {

        super(); // Super Constructor
        this.setStyle(Styles.foodPage);
        Text pageTitleLabel = new Text("Food Items");
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

        // Left Side - The List of Entered Food
        StackPane foodListContainer = new StackPane();
        foodListContainer.setStyle(Styles.foodListContainer);
        ListView<HBox> foodList = new ListView<HBox>();
        foodList.setPrefWidth(Values.foodListWidth);
        foodList.setStyle(Styles.foodList);
        foodList.getItems().add(new HBox());
        foodListContainer.getChildren().add(foodList);

        // Right Side - How the user enters the food
        GridPane foodItemEntry = new GridPane(); // Container for right side
        Text foodNameLabel = new Text("Name");
        Text foodWeightLabel = new Text("Weight");
        TextField foodNameEnt = new TextField();
        foodNameEnt.setPromptText("ex. Burger");
        TextField foodWeightEnt = new TextField();
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
                HBox emptySpace = new HBox();
                HBox.setHgrow(emptySpace, Priority.ALWAYS);
                Text foodItemWeight = new Text(weight + " oz.");
                foodItemFrame.getChildren().addAll(foodItemName, emptySpace, foodItemWeight);
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
        this.setRight(foodItemEntry);
        this.setLeft(foodListContainer);
        this.setTop(pageTitle);
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
}
