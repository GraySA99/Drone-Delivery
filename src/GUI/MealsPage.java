package GUI;

import Food.Food;
import Food.Meal;
import Simulation.DataTransfer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MealsPage extends BorderPane {

    private VBox foodFrame;
    private HashMap<Food, HBox> foodItems;
    private ListView<HBox> mealsList;
    private StackPane mealsListContainer;
    private Text mealsNameLabel, mealsProbLabel;
    private TextField mealsNameEnt, mealsProbEnt;
    private Button mealsAddItem, mealsRemoveItem;
    private ScrollPane foodScrollFrame;
    private HBox mealsNameFrame, mealsProbFrame, mealsBtnFrame;
    private VBox mealEntryFrame, foodItemsFrame;

    public MealsPage() {

        super(); // Super Constructor

        foodItems = new HashMap<>();

        // Right Side
        mealsListContainer = new StackPane();
        mealsList = new ListView<>();
        mealsList.getItems().add(new HBox());
        mealsListContainer.getChildren().add(mealsList);

        // Center
        foodItemsFrame = new VBox();
        foodFrame = new VBox();
        foodScrollFrame = new ScrollPane();
        foodScrollFrame.setContent(foodFrame);
        foodScrollFrame.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Left Side
        mealEntryFrame = new VBox();
        mealsNameFrame = new HBox();
        mealsNameLabel = new Text("Name: ");
        mealsNameEnt = new TextField();
        mealsNameFrame.getChildren().addAll(new ESHBox(), mealsNameLabel, new ESHBox(), mealsNameEnt, new ESHBox());

        mealsProbFrame = new HBox();
        mealsProbLabel = new Text("Probability: ");
        mealsProbEnt = new TextField();
        mealsProbFrame.getChildren().addAll(new ESHBox(), mealsProbLabel, new ESHBox(), mealsProbEnt, new ESHBox());

        mealsBtnFrame = new HBox();
        mealsAddItem = new Button("Add");
        mealsRemoveItem = new Button("Remove");
        mealsBtnFrame.getChildren().addAll(new ESHBox(), mealsAddItem, new ESHBox(), mealsRemoveItem, new ESHBox());
        mealsAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = mealsNameEnt.getText();
                String prob = mealsProbEnt.getText();

                if (DataTransfer.getFoodItems() != null && !name.strip().isBlank() && !prob.strip().isBlank()
                        && isNumeric(prob)
                        && DataTransfer.getMeal(name) == null) {

                    for (HBox hb : foodItems.values()) {

                        if (((CheckBox)hb.getChildren().get(0)).isSelected()) {

                            String qty = ((TextField)hb.getChildren().get(5)).getText();
                            if (!isNumeric(qty))
                                return;
                        }
                    }

                    HashMap<Food, Integer> tempFoodList = new HashMap<>();

                    for (Food food : foodItems.keySet()) {

                        if (((CheckBox)foodItems.get(food).getChildren().get(0)).isSelected()) {

                            int qty = Integer.parseInt(
                                    ((TextField)foodItems.get(food).getChildren().get(5)).getText()
                            );

                            tempFoodList.put(new Food(food.getName(), food.getWeight()), qty);
                        }
                    }

                    DataTransfer.addMeal(new Meal(name, tempFoodList, Double.parseDouble(prob)));

                    HBox frame = new HBox();
                    Text frameName = new Text(name);
                    Text frameProb = new Text(prob + "%");
                    frame.getChildren().addAll(frameName, new ESHBox(), frameProb);

                    if (mealsList.getItems().size() == 1 && mealsList.getItems().get(0).getChildren().isEmpty()) {

                        mealsList.getItems().clear();
                    }
                    mealsList.getItems().add(frame);

                    mealsNameEnt.clear();
                    mealsProbEnt.clear();
                    for (HBox hb : foodItems.values()) {

                        CheckBox tempCB = ((CheckBox)hb.getChildren().get(0));
                        TextField tempTF = ((TextField)hb.getChildren().get(5));
                        tempCB.setSelected(false);
                        tempTF.clear();
                        tempTF.setDisable(!tempCB.isSelected());
                    }
                }
            }
        });
        mealsRemoveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = mealsNameEnt.getText();

                if (DataTransfer.getMeal(name) != null) {

                    DataTransfer.removeMeal(DataTransfer.getMeal(name));
                    mealsList.getItems().removeIf(hb -> ((Text) hb.getChildren().get(0)).getText().equals(name));

                    if (mealsList.getItems().isEmpty())
                        mealsList.getItems().add(new HBox());

                    mealsNameEnt.clear();
                    mealsProbEnt.clear();
                    for (HBox hb : foodItems.values()) {

                        CheckBox tempCB = ((CheckBox)hb.getChildren().get(0));
                        TextField tempTF = ((TextField)hb.getChildren().get(4));
                        tempCB.setSelected(false);
                        tempTF.clear();
                        tempTF.setDisable(!tempCB.isSelected());
                    }
                }
            }
        });

        mealEntryFrame.getChildren().addAll(mealsNameFrame, mealsProbFrame, mealsBtnFrame);

        this.setLeft(mealEntryFrame);
        this.setCenter(foodScrollFrame);
        this.setRight(mealsListContainer);

        initFromFile("");
        refresh();
    }

    public void refresh() {

        double pageWidth = Values.windowWidth * (1 - Values.sideMenuWidthPercent);
        double pageHeight = Values.windowHeight;
        this.setMaxWidth(pageWidth);
        this.setPrefWidth(pageWidth);
        this.setPrefHeight(pageHeight);

        mealsListContainer.setMaxWidth(pageWidth * Values.mealsPageListWidthPercent);
        mealsListContainer.setMaxHeight(pageHeight * Values.mealsPageListHeightPercent);
        mealsList.setPrefWidth(pageWidth * Values.mealsPageListWidthPercent);
        mealsList.setPrefHeight(pageHeight * Values.mealsPageListHeightPercent);
        BorderPane.setAlignment(mealsListContainer, Pos.CENTER_LEFT);

        mealEntryFrame.setPrefWidth(pageWidth * Values.mealsPageEntryFrameWidthPercent);
        mealEntryFrame.setMaxHeight(pageHeight * Values.mealsPageEntryFrameHeightPercent);
        BorderPane.setAlignment(mealEntryFrame, Pos.CENTER_RIGHT);

        foodScrollFrame.setMaxWidth(pageWidth * Values.mealsPageScrollFrameWidthPercent);
        foodScrollFrame.setMaxHeight(pageHeight * Values.mealsPageScrollFrameHeightPercent);
        BorderPane.setAlignment(foodScrollFrame, Pos.CENTER_LEFT);


        // Styles
        this.setStyle(Styles.mealsPage);
        mealsListContainer.setStyle(Styles.mealsPageMealsListContainer);
        mealEntryFrame.setStyle(Styles.mealsPageEntryFrame);

    }

    public void setFoodFrame() {

        foodFrame.getChildren().clear();

        if (DataTransfer.getFoodItems() != null) {

            for (Food food : DataTransfer.getFoodItems()) {

                HBox foodFrameRow = new HBox();
                HBox.setHgrow(foodFrameRow, Priority.ALWAYS);
                CheckBox isItemIn = new CheckBox();
                Text foodName = new Text(food.getName().substring(0,1).toUpperCase()
                        .concat(food.getName().substring(1,food.getName().length())));
                Text qtyLabel = new Text("Qty: ");
                TextField qtyEnt = new TextField();
                qtyEnt.setPromptText("0");
                foodFrameRow.getChildren().addAll(isItemIn, new ESHBox(), foodName, new ESHBox(),
                        qtyLabel, qtyEnt);
                foodItems.put(food, foodFrameRow);

                qtyEnt.clear();
                qtyEnt.setDisable(!isItemIn.isSelected());
                isItemIn.setOnMouseClicked(evt -> {

                    qtyEnt.clear();
                    qtyEnt.setDisable(!isItemIn.isSelected());
                });

                foodFrame.getChildren().add(foodFrameRow);
            }

            if (foodFrame.getChildren().isEmpty()) {

                foodFrame.getChildren().add(new HBox());
            }
        }
    }

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

            while (fileIn.hasNextLine() && !fileLine.equals("@Meals")) { fileLine = fileIn.nextLine(); }
            if (!fileIn.hasNextLine()) { return; }

            fileLine = fileIn.nextLine();
            mealsList.getItems().clear();
            foodFrame.getChildren().clear();

            String name = "";
            String prob = "";
            HashMap<Food, Integer> tempFoodList = null;

            while (fileIn.hasNextLine()) {

                boolean workedWithFood = false;

                if (fileLine.strip().equals("@/Meals")) { break; }

                if (!fileLine.contains("*")) {
                    name = fileLine.strip().split("&")[0];
                    prob = fileLine.strip().split("&")[1];
                    tempFoodList = new HashMap<>();
                } else {

                    while (fileLine.contains("*")) {

                        fileLine = fileLine.replace("*", "");
                        Food tempFood = DataTransfer.getFood(fileLine.strip().split("&")[0]);
                        int qty = Integer.parseInt(fileLine.strip().split("&")[1]);

                        tempFoodList.put(tempFood, qty);

                        fileLine = fileIn.nextLine();
                    }

                    HBox frame = new HBox();
                    Text frameName = new Text(name);
                    Text frameProb = new Text(prob + "%");
                    frame.getChildren().addAll(frameName, new ESHBox(), frameProb);

                    mealsList.getItems().add(frame);
                    DataTransfer.addMeal(new Meal(name, tempFoodList, Double.parseDouble(prob)));

                    workedWithFood = true;
                }

                if (!workedWithFood)
                    fileLine = fileIn.nextLine();
            }
        } catch (FileNotFoundException e) {

            System.out.println("Problem With File");
            e.printStackTrace();
        }
    }
}
