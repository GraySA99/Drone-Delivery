package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Set;

public class FoodPage extends BorderPane {

    private HashMap<String, HBox> foodItems; // List of entered food items

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

        foodItems = new HashMap<>();

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

                // If name entry doesn't already exist
                // AND name entry isn't blank
                // AND weight is an numeric value
                if (!foodItems.containsKey(foodNameEnt.getText().toLowerCase())
                        && !foodNameEnt.getText().isBlank()
                        && isNumeric(foodWeightEnt.getText())) {

                    if (foodItems.isEmpty()) {
                        foodList.getItems().clear();
                    } // If list is empty then get rid of the blank item in the list view

                    // Add new item to list view
                    HBox temp = new HBox();
                    Text name = new Text(foodNameEnt.getText());
                    Text weight = new Text(foodWeightEnt.getText() + " oz");
                    HBox emptySpace = new HBox();
                    HBox.setHgrow(emptySpace, Priority.ALWAYS);
                    temp.getChildren().addAll(name, emptySpace, weight);
                    foodList.getItems().add(temp);
                    foodItems.put(foodNameEnt.getText().toLowerCase(), temp);

                    // Clear Entry Fields
                    foodNameEnt.clear();
                    foodWeightEnt.clear();
                }
            }
        });

        // Remove food item from list on left side if it exists
        foodRemoveItemBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                // If the item actually exists
                if (foodItems.containsKey(foodNameEnt.getText().toLowerCase())) {

                    // Remove item from the list view and the HashMap
                    foodList.getItems().remove(foodItems.get(foodNameEnt.getText().toLowerCase()));
                    foodItems.remove(foodNameEnt.getText().toLowerCase());

                    // If the last item was just removed then add a blank to keep the list view visible
                    if (foodList.getItems().isEmpty()) {

                        foodList.getItems().add(new HBox());
                    }

                    // Clear Entries
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

    // Function: getFoodItems
    // Purpose: returns the names of all food items
    // Input: NA
    // Returns: Set of Strings (Set<String>)
    public Set<String> getFoodItems() {

        return foodItems.keySet();
    }
}
