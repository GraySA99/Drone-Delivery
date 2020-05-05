package GUI;

import Food.Food;

import Simulation.DataTransfer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 ** This class contains the information, layout, and logic for the food page of the GUI
 **/

public class FoodPage extends BorderPane {

    private ListView<HBox> foodList;
    private TextField foodNameEnt;
    private TextField foodWeightEnt;
    private StackPane foodListContainer;
    private VBox foodItemEntry;
    private Text foodNameLabel, foodWeightLabel;
    private Button foodAddItemBtn, foodRemoveItemBtn;
    private HBox foodBtnFrame;

    public FoodPage() {

        super(); // Super Constructor

        // Right Side - The List of Entered Food
        foodListContainer = new StackPane();
        foodList = new ListView<HBox>();
        foodList.getItems().add(new HBox());
        foodListContainer.getChildren().add(foodList);

        // Left Side - How the user enters the food
        foodItemEntry = new VBox();

        foodNameLabel = new Text("Name: ");
        foodNameEnt = new TextField();
        foodNameEnt.setPromptText("ex. Burger");

        foodWeightLabel = new Text("Weight: ");
        foodWeightEnt = new TextField();
        foodWeightEnt.setPromptText("ex. 10");

        foodBtnFrame = new HBox();
        foodAddItemBtn = new Button("Add");
        foodRemoveItemBtn = new Button("Remove");
        foodBtnFrame.getChildren().addAll(new ESHBox(), foodAddItemBtn, new ESHBox(), foodRemoveItemBtn, new ESHBox());
        foodAddItemBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = foodNameEnt.getText();
                String weight = foodWeightEnt.getText();

                // Data Filtering
                if (!name.strip().isBlank() && !weight.strip().isBlank()
                        && isNumeric(weight)
                        && DataTransfer.getFood(name) == null) {

                    DataTransfer.addFood(new Food(name, Double.parseDouble(weight)));
                }

                // This adds an entry into the list foodList which is a ListView of HBox's
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

                // In order for the ListView to show up there needs to be one entry in it
                // So if the first entry is blank, that's just a filler HBox and needs deleted
                if (foodList.getItems().size() == 1 && foodList.getItems().get(0).getChildren().isEmpty()) {
                    foodList.getItems().clear();
                }
                foodList.getItems().add(foodItemFrame);

                foodNameEnt.clear();
                foodWeightEnt.clear();
            }
        });
        foodRemoveItemBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = foodNameEnt.getText();

                if (DataTransfer.getMeal(name) != null) {

                    DataTransfer.removeFood(DataTransfer.getFood(name));
                    foodList.getItems().removeIf(hb -> ((Text) hb.getChildren().get(0)).getText().equals(name));

                    // In order for the ListView to show up there needs to be one entry in it
                    // So if the list is empty after the remove then a blank is added
                    if (foodList.getItems().isEmpty())
                        foodList.getItems().add(new HBox());

                    foodNameEnt.clear();
                    foodWeightEnt.clear();
                }
            }
        });

        foodItemEntry.getChildren().addAll(foodNameLabel, foodNameEnt, foodWeightLabel, foodWeightEnt, foodBtnFrame);

        this.setLeft(foodItemEntry);
        this.setRight(foodListContainer);

        // Loading settings from files
        initFromFile("");
    }


    public ListView<HBox> getFoodList() {
        return foodList;
    }

    // This long method is just for setting the sizing and layout of each element in the page
    // It is called on every screen resize
    public void refresh() {

        double pageWidth = Values.windowWidth * (1 - Values.sideMenuWidthPercent);
        double pageHeight = Values.windowHeight;
        this.setMaxWidth(pageWidth);
        this.setPrefWidth(pageWidth);
        this.setPrefHeight(pageHeight);

        foodListContainer.setMaxWidth(pageWidth * Values.foodPageFoodListWidthPercent);
        foodListContainer.setMaxHeight(pageHeight * Values.foodPageFoodListHeightPercent);
        foodList.setPrefWidth(pageWidth * Values.foodPageFoodListWidthPercent);
        foodList.setPrefHeight(pageHeight * Values.foodPageFoodListHeightPercent);
        BorderPane.setAlignment(foodListContainer, Pos.CENTER_LEFT);

        foodItemEntry.setPrefWidth(pageWidth * Values.foodPageFoodItemEntryWidthPercent);
        foodItemEntry.setMaxHeight(pageHeight * Values.foodPageFoodItemEntryHeightPercent);
        foodItemEntry.setPrefHeight(pageHeight * Values.foodPageFoodItemEntryHeightPercent);
        BorderPane.setAlignment(foodItemEntry, Pos.CENTER_RIGHT);

        // Styles
        this.setStyle(Styles.foodPage);
        foodListContainer.setStyle(Styles.foodPageFoodListContainer);
        foodNameLabel.setStyle(Styles.foodPageFoodLabel + "-fx-font-size: "
                + (foodNameEnt.getHeight() * Values.foodPageFontSize) + ";\n");
        foodWeightLabel.setStyle(Styles.foodPageWeightLabel + "-fx-font-size: "
                + (foodWeightEnt.getHeight() * Values.foodPageFontSize) + ";\n");
        foodItemEntry.setStyle(Styles.foodPageFoodItemEntry);
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

    // Load settings for food page from a specified file or default file
    public void initFromFile(String filename) {

        try {
            FileInputStream fis = new FileInputStream(Values.defaultFileName);
            if(!filename.equals(""))
                fis = new FileInputStream(filename);

            Scanner fileIn = new Scanner(fis);
            if (!fileIn.hasNextLine()) { return; }
            String fileLine = fileIn.nextLine();

            // See defaults_universal.dd for how we layout stored data
            while (fileIn.hasNextLine() && !fileLine.equals("@Food")) { fileLine = fileIn.nextLine(); }
            if (!fileIn.hasNextLine()) { return; }

            fileLine = fileIn.nextLine();
            foodList.getItems().clear();
            while (fileIn.hasNextLine()) {

                // Read new food until there is none left
                if (fileLine.strip().equals("@/Food")) { break; }

                String name = fileLine.strip().split("&")[0];
                String weight = fileLine.strip().split("&")[1]; // in lbs
                Double weightInOz = Double.parseDouble(weight) * 16; // in oz

                HBox foodItemFrame = new HBox();
                Text foodItemName = new Text(name);
                Text foodItemWeight = new Text(weightInOz.toString() + " oz.");
                foodItemFrame.getChildren().addAll(foodItemName, new ESHBox(), foodItemWeight);
                foodItemFrame.setOnMouseClicked(evt -> {

                    String foodName = ((Text)foodItemFrame.getChildren().get(0)).getText();
                    String foodWeight = ((Text)foodItemFrame.getChildren().get(2)).getText()
                            .replaceAll(" oz.", "");

                    foodNameEnt.setText(foodName);
                    foodWeightEnt.setText(foodWeight);
                });

                foodList.getItems().add(foodItemFrame);

                //added to datatransfer in pounds
                DataTransfer.addFood(new Food(name, Double.parseDouble(weight)));

                fileLine = fileIn.nextLine();
            }
        } catch (FileNotFoundException e) {

            System.out.println("Problem With File");
            e.printStackTrace();
        }
    }
}
