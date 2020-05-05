package GUI;

import Food.Food;
import Food.Meal;
import Simulation.Simulation;
import Simulation.DataTransfer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

/**
 ** This class controls the left side menu of the GUI and how it loads pages in and out
 **/

public class SideMenu extends ToolBar {

    private HashMap<Button, Integer> menues;
    private Pane[] pages;
    private Integer activeScene;
    private Button sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn;
    private Button start, save, results, load;
    private HBox title;
    private Text titleLabel;

    public SideMenu(Pane[] panes, BorderPane rM, Stage pM) {

        // Set Up
        super();
        menues = new HashMap<Button, Integer>();
        activeScene = 1;
        pages = panes.clone();

        title = new HBox();
        HBox.setHgrow(title, Priority.ALWAYS);
        titleLabel = new Text("AzureSim");
        title.getChildren().addAll(new ESHBox(), titleLabel, new ESHBox());

        sideMenuMapBtn = new Button("Map");
        sideMenuFoodBtn = new Button("Food");
        sideMenuMealsBtn = new Button("Meals");
        sideMenuShiftsBtn = new Button("Shifts");

        menues.put(sideMenuMapBtn, Values.mapMenuID);
        menues.put(sideMenuFoodBtn, Values.foodMenuID);
        menues.put(sideMenuMealsBtn, Values.mealsMenuID);
        menues.put(sideMenuShiftsBtn, Values.shiftsMenuID);

        SetupActionButtons();
        SideMenuOnHover(new Button[] {sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn,
                start, results, save, load});
        SideMenuOnClick(new Button[]{sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn});

        this.getItems().addAll(title, sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn,
                new ESVBox(), start, results, save, load);

        resizeWindow();
    }

    private void SetupActionButtons() {

        start = new Button("Start");
        save = new Button("Save");
        results = new Button("Results");
        load = new Button("Load");

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);

                BorderPane subRoot = new BorderPane();
                Text dialogText = new Text("Simulation is Finished");
                Button okBtn = new Button("Ok");
                okBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {

                        dialog.close();
                    }
                });
                subRoot.setCenter(dialogText);
                HBox okBtnFrame = new HBox();
                okBtnFrame.getChildren().addAll(new ESHBox(), okBtn, new ESHBox());
                subRoot.setBottom(okBtnFrame);
                Scene dialogScene = new Scene(subRoot, 300, 200);
                dialog.setScene(dialogScene);
                dialog.showAndWait();

                Values.simulation = new Simulation();
                Values.simulation.runSimulation();

            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter for our .dd files
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".DD files (*.dd)", "*.dd");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialogue
                File file = fileChooser.showSaveDialog(Values.primaryStage);

                if(file != null) {
                    writeSimSettingsToFile(file);
                }
            }
        });

        results.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Values.primaryStage.getScene().setRoot(new ResultsPage());
            }
        });

        // is supposed to put the rules in the specified .dd file into the simulation
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //Platform.exit(); //System.exit(0);
                FileChooser fileChooser = new FileChooser();

                //Set extension filter for our .dd files
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".DD files (*.dd)", "*.dd");
                fileChooser.getExtensionFilters().add(extFilter);

                // Show open dialog
                File openFile = fileChooser.showOpenDialog(Values.primaryStage);

                if(openFile != null) {
                    String path = openFile.getAbsolutePath();
                    //System.out.println("PATH: " + path);
                    setSimSettings(path);
                }
            }
        });
    }

    public void resizeWindow() {

        // Styles
        this.setStyle(Styles.sideMenu);
        title.setStyle(Styles.sideMenuTitle);
        titleLabel.setStyle(Styles.sideMenuTitleText);

        // Dimensions
        double menuWidth = Values.windowWidth * Values.sideMenuWidthPercent;
        double menuHeight = this.getHeight();

        this.setOrientation(Orientation.VERTICAL);
        this.setMinWidth(menuWidth);

        sideMenuMapBtn.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        sideMenuMapBtn.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);
        sideMenuFoodBtn.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        sideMenuFoodBtn.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);
        sideMenuMealsBtn.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        sideMenuMealsBtn.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);
        sideMenuShiftsBtn.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        sideMenuShiftsBtn.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);
        start.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        start.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);
        results.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        results.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);
        save.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        save.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);
        load.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        load.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);

        // Buttons
        SideMenuOnHover(new Button[] {sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn,
                start, results, save, load});
        SideMenuOnClick(new Button[] {sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn});
    }

    //Sets the simulation settings through .dd file the user chooses
    private void setSimSettings(String filepath) {
        Values.foodPage.initFromFile(filepath);
        Values.mapPage.initFromFile(filepath);
        Values.mealsPage.initFromFile(filepath);
        Values.shiftsPage.initFromFile(filepath);
    }

    //Writes str to .dd file "file"
    private void writeSimSettingsToFile(File file)
    {
        try {
            PrintWriter writer = new PrintWriter(file);
            //writer.println(str);

            // Print food information to file
            writer.println("@Food");
            for(int listItem = 0; listItem < Values.foodPage.getFoodList().getItems().size(); listItem++) {
                String foodName = ((Text) Values.foodPage.getFoodList().getItems().get(listItem).getChildren().get(0)).getText();
                String foodWeight = ((Text) Values.foodPage.getFoodList().getItems().get(listItem).getChildren().get(2)).getText()
                        .replaceAll(" oz.", ""); // in oz
                Double weight = Double.valueOf(foodWeight) / 16; // converts to lbs
                foodWeight = weight.toString(); // in lbs
                writer.println(foodName + "&" + foodWeight);
            }
            writer.println("@/Food\n\n");

            // Get meals and print them to file
            writer.println("@Meals");
            for(int i = 0; i < DataTransfer.getNumMeals(); i++) {
                Meal tempMeal = DataTransfer.getMeal(i);
                String name = tempMeal.getName();
                double prob = tempMeal.getProbability();
                writer.println(name + "&" + prob);

                for(Food food : tempMeal.getFoodItems()) {
                    writer.println("*" + food.getName() + "&" + tempMeal.getFoodItemQty(food));
                }
            }
            writer.println("@/Meals\n\n");

            // Get map waypoints and print them to file
            writer.println("@Waypoint");
            for(int i = 0; i < Values.mapPage.getDPList().getItems().size(); i++) {
                String name = ((Text) Values.mapPage.getDPList().getItems().get(i).getChildren().get(0)).getText();
                String coords = ((Text) Values.mapPage.getDPList().getItems().get(i).getChildren().get(2)).getText();
                String lat = coords.substring(1, coords.length() - 1).split(", ")[0];
                String lon = coords.substring(1, coords.length() - 1).split(", ")[1];
                writer.println(name + "&" + lat + "," + lon);
            }
            writer.println("@/Waypoint\n\n");

            // Get shifts and print them to file
            writer.println("@Shifts");
            int numShifts = DataTransfer.getNumShifts();
            int numSims = DataTransfer.getNumSimulations();
            writer.println(numShifts + "&" + numSims);
            for(int i = 1; i <= numShifts; i++) {
                writer.println("*" + i + "&" + DataTransfer.getShift(i));
            }
            writer.println("@/Shifts\n\n");

            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void SideMenuOnHover(Button[] buttons) {

        for (Button b : buttons) {

            b.styleProperty().bind(
                    Bindings.when(b.hoverProperty())
                            .then(
                                    new SimpleStringProperty(getButtonStyle(b, true))
                            )
                            .otherwise(
                                    new SimpleStringProperty(getButtonStyle(b, false))
                            )
            );
        }
    }

    private void SideMenuOnClick(Button[] buttons) {

        for (Button b : buttons) {

            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {

                    activeScene = menues.get(b);

                    MultiThreadRefresh MTR = new MultiThreadRefresh();
                    MTR.start();

                    try {
                        MTR.join();
                        if (activeScene.equals(Values.mealsMenuID)) {

                            ((MealsPage)(pages[activeScene-1])).setFoodFrame();
                        }
                    } catch (InterruptedException ie) {};

                    Values.rootPage.setCenter(pages[activeScene-1]);
                    for (Button btn : menues.keySet()) {

                        btn.styleProperty().unbind();
                        SideMenuOnHover(new Button[]{btn});
                    }
                }
            });
        }
    }

    private String getButtonStyle(Button b, boolean isHover) {

        if (activeScene.equals(menues.get(b))) {

            return Styles.sideMenuBtnActive;
        }

        return isHover
                ? Styles.sideMenuBtnHover
                : Styles.sideMenuBtn;
    }
}
