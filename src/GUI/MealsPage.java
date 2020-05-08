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
        mealsNameFrame = new HBox();
        mealsNameLabel = new Text("Name: ");
        mealsNameEnt = new TextField();

        mealsProbLabel = new Text("Probability: ");
        mealsProbEnt = new TextField();

        mealsBtnFrame = new HBox();
        mealsAddItem = new Button("Add");
        mealsRemoveItem = new Button("Remove");
        mealsBtnFrame.getChildren().addAll(new ESHBox(), mealsAddItem, new ESHBox(), mealsRemoveItem, new ESHBox());
        mealsAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = mealsNameEnt.getText();
                String prob = mealsProbEnt.getText();

                // Data Filtering
                if (DataTransfer.getFoodItems() != null && !name.strip().isBlank() && !prob.strip().isBlank()
                        && isNumeric(prob)
                        && DataTransfer.getMeal(name) == null) {

                    for (HBox hb : foodItems.values()) {

                        if (((CheckBox)hb.getChildren().get(0)).isSelected()) {

                            String qty = ((TextField)hb.getChildren().get(4)).getText();
                            if (!isNumeric(qty))
                                return;
                        }
                    }

                    HashMap<Food, Integer> tempFoodList = new HashMap<>();

                    for (Food food : foodItems.keySet()) {

                        if (((CheckBox)foodItems.get(food).getChildren().get(0)).isSelected()) {

                            int qty = Integer.parseInt(
                                    ((TextField)foodItems.get(food).getChildren().get(4)).getText()
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
                        TextField tempTF = ((TextField)hb.getChildren().get(4));
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

        mealEntryFrame = new VBox();
        mealEntryFrame.getChildren().addAll(
                mealsNameLabel,
                mealsNameEnt,
                mealsProbLabel,
                mealsProbEnt,
                mealsBtnFrame
        );

        this.setLeft(mealEntryFrame);
        this.setCenter(foodScrollFrame);
        this.setRight(mealsListContainer);

        initFromFile("");
        refresh();
    }

    // This long method is just for setting the sizing and layout of each element in the page
    // It is called on every screen resize
    public void refresh() {

        double pageWidth = Values.windowWidth * (1 - Values.sideMenuWidthPercent);
        double pageHeight = Values.windowHeight;
        this.setMaxWidth(pageWidth);
        this.setMinWidth(pageWidth);
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
        foodFrame.setPrefWidth(pageWidth * Values.mealsPageScrollFrameWidthPercent * Values.mealsPageScrollItemsWidthPercent);
        foodFrame.setPrefHeight(pageHeight * Values.mealsPageScrollFrameHeightPercent);
        BorderPane.setAlignment(foodScrollFrame, Pos.CENTER_LEFT);

        // Food Items
        double foodScrollWidth = foodItemsFrame.getWidth();
        double foodScrollHeight = foodItemsFrame.getHeight();

        for (Node n : foodItemsFrame.getChildren()) {

            HBox foodItem = (HBox)n;
            CheckBox cb = ((CheckBox)foodItem.getChildren().get(0));
            Text foodName = ((Text)foodItem.getChildren().get(2));
            Text qtyLabel = ((Text)foodItem.getChildren().get(4));
            TextField qtyEnt = ((TextField)foodItem.getChildren().get(4));

            foodItem.setPrefWidth(foodScrollWidth * Values.mealsPageFoodItemFrameWidthPercent);
            foodItem.setMaxHeight(foodScrollHeight * Values.mealsPageFoodItemFrameHeightPercent);
            foodItem.setMaxHeight(foodScrollHeight * Values.mealsPageFoodItemFrameHeightPercent);

            cb.setPrefSize(foodScrollHeight * Values.mealsPageCheckBoxSidePercent,
                    foodScrollHeight * Values.mealsPageCheckBoxSidePercent);

            qtyEnt.setPrefWidth(foodScrollWidth * Values.mealsPageQtyEntWidthPercent);
            qtyEnt.setPrefHeight(foodScrollHeight * Values.mealsPageQtyEntHeightPercent);

            foodItem.setStyle(Styles.mealsPageFoodItemFrame);
        }

        // Styles
        this.setStyle(Styles.mealsPage);
        mealsListContainer.setStyle(Styles.mealsPageMealsListContainer);
        foodFrame.setStyle(Styles.test);
        mealEntryFrame.setStyle(Styles.mealsPageEntryFrame);
        mealsBtnFrame.setStyle(Styles.mealsPageButtonFrame);
        mealsNameEnt.setStyle(Styles.mealsPageEntryBox);
        mealsProbEnt.setStyle(Styles.mealsPageEntryBox);

    }

    // Populates the list of all Food Items available into the list in the middle of the GUI
    public void setFoodFrame() {

        foodFrame.getChildren().clear();

        if (DataTransfer.getFoodItems() != null) {

            for (Food food : DataTransfer.getFoodItems()) {

                HBox foodFrameRow = new HBox();
                HBox.setHgrow(foodFrameRow, Priority.ALWAYS);
                CheckBox isItemIn = new CheckBox();
                Text foodName = new Text(food.getName().substring(0,1).toUpperCase()
                        .concat(food.getName().substring(1, food.getName().length())));
                Text qtyLabel = new Text("Qty: ");
                TextField qtyEnt = new TextField();
                qtyEnt.setPromptText("0");

                foodFrameRow.getChildren().addAll(isItemIn, foodName, new ESHBox(),
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

    // Checks if a string is numeric
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

            FileInputStream fis;
            if(!filename.equals(""))
                fis = new FileInputStream(filename);
            else
                fis = new FileInputStream(Values.defaultFileName);

            Scanner fileIn = new Scanner(fis);
            if (!fileIn.hasNextLine()) { return; }
            String fileLine = fileIn.nextLine();

            // See defaults_universal.dd for how we layout stored data
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
