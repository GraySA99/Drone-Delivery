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

        foodItemEntry.getChildren().addAll(foodNameLabel, foodNameEnt, foodWeightLabel, foodWeightEnt, foodBtnFrame);

        this.setLeft(foodItemEntry);
        this.setRight(foodListContainer);

        // initFromFile will load defaults now
        initFromFile("");
    }


    public ListView<HBox> getFoodList() {
        return foodList;
    }

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
        foodItemEntry.setPrefHeight(pageHeight * Values.foodPageFoodItemEntryHeightPercent);

        double foodItemEntryWidth = foodItemEntry.getWidth();
        double foodItemEntryHeight = foodItemEntry.getHeight();

        foodNameEnt.setPrefWidth(foodItemEntryWidth * Values.foodPageFoodNameEntWidthPercent);
        foodNameEnt.setPrefHeight(foodItemEntryHeight * Values.foodPageFoodNameEntHeightPercent);
        foodWeightEnt.setPrefWidth(foodItemEntryWidth * Values.foodPageFoodWeightEntWidthPercent);
        foodWeightEnt.setPrefHeight(foodItemEntryHeight * Values.foodPageFoodWeightEntHeightPercent);
        foodAddItemBtn.setPrefWidth(foodItemEntryWidth * Values.foodPageBtnWidthPercent);
        foodAddItemBtn.setPrefHeight(foodItemEntryHeight * Values.foodPageBtnHeightPercent);
        foodRemoveItemBtn.setPrefWidth(foodItemEntryWidth * Values.foodPageBtnWidthPercent);
        foodRemoveItemBtn.setPrefHeight(foodItemEntryHeight * Values.foodPageBtnHeightPercent);

        // Styles
        this.setStyle(Styles.foodPage);
        foodListContainer.setStyle(Styles.foodPageFoodListContainer);
        foodNameLabel.setStyle(Styles.foodPageFoodLabel + "-fx-font-size: "
                + (foodNameEnt.getHeight() * Values.foodPageFontSize) + ";\n");
        foodWeightLabel.setStyle(Styles.foodPageWeightLabel + "-fx-font-size: "
                + (foodWeightEnt.getHeight() * Values.foodPageFontSize) + ";\n");
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

    public void initFromFile(String filename) {

        try {
            FileInputStream fis = new FileInputStream(Values.defaultFileName);
            if(!filename.equals(""))
                fis = new FileInputStream(filename);

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
