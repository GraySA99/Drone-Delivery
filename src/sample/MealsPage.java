package sample;

import Food.Food;
import Food.Meal;
import Simulation.DataTransfer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    public MealsPage() {

        super(); // Super Constructor
        this.setStyle(Styles.mealsPage);

        PageTitle pageTitle = new PageTitle("Meals");

        foodItems = new HashMap<>();

        // Left Side
        StackPane mealsListContainer = new StackPane();
        mealsListContainer.setStyle(Styles.foodListContainer);
        mealsList = new ListView<>();
        mealsList.setPrefWidth(550);
        mealsList.setStyle(Styles.mealsList);
        mealsList.getItems().add(new HBox());

        // Right Side
        GridPane mealEntry = new GridPane();
        mealEntry.setStyle(Styles.mealEntry);

        foodFrame = new VBox();
        foodFrame.setStyle(Styles.foodFrame);
        ScrollPane foodScrollFrame = new ScrollPane();
        foodScrollFrame.setContent(foodFrame);
        foodScrollFrame.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        foodScrollFrame.setStyle(Styles.foodScrollFrame);
        foodScrollFrame.setPrefSize(250, Values.foodScrollFrameHeight);
        Text mealsNameLabel = new Text("Name");
        Text mealsProbLabel = new Text("Probability");
        TextField mealsNameEnt = new TextField();
        TextField mealsProbEnt = new TextField();
        Button mealsAddItem = new Button("Add");
        Button mealsRemoveItem = new Button("Remove");

        mealEntry.add(mealsNameLabel, 0, 0, 1, 1);
        mealEntry.add(mealsNameEnt, 0, 1, 1, 1);
        mealEntry.add(mealsProbLabel, 1, 0, 1, 1);
        mealEntry.add(mealsProbEnt, 1, 1, 1, 1);
        mealEntry.add(foodScrollFrame, 0, 2, 2, 2);
        mealEntry.add(mealsAddItem, 0, 4, 1, 1);
        mealEntry.add(mealsRemoveItem, 1, 4, 1, 1);

        mealsAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                String name = mealsNameEnt.getText();
                String prob = mealsProbEnt.getText();

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

        this.setRight(mealEntry);
        this.setLeft(mealsList);
        this.setTop(pageTitle);

        initFromFile();
    }

    public void setFoodFrame() {

        foodFrame.getChildren().clear();

        if (DataTransfer.getFoodItems() != null) {

            for (Food food : DataTransfer.getFoodItems()) {

                HBox foodFrameRow = new HBox();
                HBox.setHgrow(foodFrameRow, Priority.ALWAYS);
                foodFrameRow.setStyle(Styles.foodFrameRow);
                CheckBox isItemIn = new CheckBox();
                Text foodName = new Text(food.getName().substring(0,1).toUpperCase()
                        .concat(food.getName().substring(1,food.getName().length())));
                Text qtyLabel = new Text("Qty");
                TextField qtyEnt = new TextField();
                qtyEnt.setPromptText("0");
                qtyEnt.setPrefSize(35, 20);
                foodFrameRow.getChildren().addAll(isItemIn, foodName, new ESHBox(), qtyLabel, qtyEnt);
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

    private void initFromFile() {

        try {

            FileInputStream fis = new FileInputStream(Values.defaultMealsFileName);
            Scanner fileIn = new Scanner(fis);
            if (!fileIn.hasNextLine()) { return; }
            String fileLine = fileIn.nextLine();

            while (fileIn.hasNextLine() && !fileLine.equals("@Meals")) { fileLine = fileIn.nextLine(); }
            if (!fileIn.hasNextLine()) { return; }

            fileLine = fileIn.nextLine();
            mealsList.getItems().clear();

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
