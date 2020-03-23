package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.HashMap;

public class MealsPage extends BorderPane {

    private VBox foodFrame;
    private HashMap<String, HBox> mealItems;
    private HashMap<String, HBox> foodItems;

    public MealsPage() {

        super(); // Super Constructor
        this.setStyle(Styles.mealsPage);
        Text pageTitleLabel = new Text("Meals");
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

        mealItems = new HashMap<>();
        foodItems = new HashMap<>();

        // Left Side
        ListView<HBox> mealsList = new ListView<>();
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

                if (!mealItems.containsKey(mealsNameEnt.getText().toLowerCase())
                    && !mealsNameEnt.getText().isBlank()
                    && isNumeric(mealsProbEnt.getText())) {

                    if (mealItems.isEmpty()) {
                        mealsList.getItems().clear();
                    }

                    HBox temp = new HBox();
                    Text name = new Text(mealsNameEnt.getText());
                    Text prob = new Text(mealsProbEnt.getText() + "%");
                    HBox emptySpace = new HBox();
                    HBox.setHgrow(emptySpace, Priority.ALWAYS);
                    temp.getChildren().addAll(name, emptySpace, prob);
                    mealsList.getItems().add(temp);
                    mealItems.put(mealsNameEnt.getText().toLowerCase(), temp);

                    for (HBox hb : foodItems.values()) {

                        ((CheckBox)hb.getChildren().get(0)).setSelected(false);
                        checkBoxActive(
                                ((CheckBox)hb.getChildren().get(0)),
                                ((TextField)hb.getChildren().get(4))
                        );
                    }

                    mealsNameEnt.clear();
                    mealsProbEnt.clear();
                }
            }
        });

        mealsRemoveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                if (mealItems.containsKey(mealsNameEnt.getText().toLowerCase())) {

                    mealsList.getItems().remove(mealItems.get(mealsNameEnt.getText().toLowerCase()));
                    mealItems.remove(mealsNameEnt.getText().toLowerCase());

                    for (HBox hb : foodItems.values()) {

                        ((CheckBox)hb.getChildren().get(0)).setSelected(false);
                        checkBoxActive(
                                ((CheckBox)hb.getChildren().get(0)),
                                ((TextField)hb.getChildren().get(4))
                        );
                    }

                    if (mealsList.getItems().isEmpty()) {

                        mealsList.getItems().add(new HBox());
                    }

                    mealsNameEnt.clear();
                    mealsProbEnt.clear();
                }
            }
        });

        this.setRight(mealEntry);
        this.setLeft(mealsList);
        this.setTop(pageTitle);
    }

    public void setFoodFrame(FoodPage foodPage) {

        foodFrame.getChildren().clear();

        for (String s : foodPage.getFoodItems()) {

            HBox foodFrameRow = new HBox();
            HBox.setHgrow(foodFrameRow, Priority.ALWAYS);
            foodFrameRow.setStyle(Styles.foodFrameRow);
            HBox.setHgrow(foodFrameRow, Priority.ALWAYS);
            CheckBox isItemIn = new CheckBox();
            Text foodName = new Text(s.substring(0,1).toUpperCase().concat(s.substring(1,s.length())));
            HBox ES = new HBox();
            HBox.setHgrow(ES, Priority.ALWAYS);
            Text qtyLabel = new Text("                   Qty");
            TextField qtyEnt = new TextField();
            qtyEnt.setPromptText("0");
            qtyEnt.setPrefSize(35, 20);
            foodFrameRow.getChildren().addAll(isItemIn, foodName, ES, qtyLabel, qtyEnt);
            foodItems.put(s, foodFrameRow);

            checkBoxActive(isItemIn, qtyEnt);
            isItemIn.setOnMouseClicked(evt -> {

                checkBoxActive(isItemIn, qtyEnt);
            });

            foodFrame.getChildren().add(foodFrameRow);
        }

        if (foodFrame.getChildren().isEmpty()) {

            foodFrame.getChildren().add(new HBox());
        }
    }

    private void checkBoxActive(CheckBox cb, TextField tf) {

        tf.clear();
        tf.setDisable(!cb.isSelected());
    }

    private boolean isNumeric(String s) {

        try {

            Double.parseDouble(s);
            return true;

        } catch (NumberFormatException e) {

            return false;
        }
    }
}
