package sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class SideMenu extends ToolBar {

    private HashMap<Button, Integer> menues;
    private Pane[] pages;
    private BorderPane rootMenu;
    private Integer activeScene;
    private ToolBar secondaryMenu;

    public SideMenu(Pane[] panes, BorderPane rM) {

        // Set Up
        super();
        menues = new HashMap<Button, Integer>();
        activeScene = 1;
        rootMenu = rM;
        Button sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn;
        pages = panes.clone();
        this.setOrientation(Orientation.VERTICAL);
        this.setMaxWidth(Values.sideMenuWidth);
        this.setPrefWidth(Values.sideMenuWidth);
        this.setMinWidth(Values.sideMenuWidth);
        this.setMaxHeight(Values.sideMenuHeight);
        this.setStyle(Styles.sideMenu);
        setUpSecondaryMenu();

        HBox title = new HBox();
        HBox titleEmptySpace = new HBox();
        HBox titleEmptySpace2 = new HBox();
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(titleEmptySpace, Priority.ALWAYS);
        HBox.setHgrow(titleEmptySpace2, Priority.ALWAYS);
        title.setMaxSize(Values.sideMenuBtnWidth, Values.sideMenuBtnHeight);
        Text titleLabel = new Text("AzureSim");
        Button titleBtn = new Button("|||");
        title.getChildren().addAll(titleEmptySpace2, titleLabel, titleEmptySpace, titleBtn);
        title.setStyle(Styles.sideMenuTitle);
        titleLabel.setStyle(Styles.sideMenuTitleText);
        HamburgerButtonSetup(titleBtn);

        sideMenuMapBtn = new Button("Map");
        sideMenuMapBtn.setMaxSize(Values.sideMenuBtnWidth, Values.sideMenuBtnHeight);
        sideMenuMapBtn.setStyle(Styles.sideMenuBtnActive);
        sideMenuFoodBtn = new Button("Food");
        sideMenuFoodBtn.setMaxSize(Values.sideMenuBtnWidth, Values.sideMenuBtnHeight);
        sideMenuFoodBtn.setStyle(Styles.sideMenuBtn);
        sideMenuMealsBtn = new Button("Meals");
        sideMenuMealsBtn.setMaxSize(Values.sideMenuBtnWidth, Values.sideMenuBtnHeight);
        sideMenuMealsBtn.setStyle(Styles.sideMenuBtn);
        sideMenuShiftsBtn = new Button("Shifts");
        sideMenuShiftsBtn.setMaxSize(Values.sideMenuBtnWidth, Values.sideMenuBtnHeight);
        sideMenuShiftsBtn.setStyle(Styles.sideMenuBtn);

        menues.put(sideMenuMapBtn, Values.mapMenuID);
        menues.put(sideMenuFoodBtn, Values.foodMenuID);
        menues.put(sideMenuMealsBtn, Values.mealsMenuID);
        menues.put(sideMenuShiftsBtn, Values.shiftsMenuID);

        SideMenuOnHover(sideMenuMapBtn);
        SideMenuOnHover(sideMenuFoodBtn);
        SideMenuOnHover(sideMenuMealsBtn);
        SideMenuOnHover(sideMenuShiftsBtn);
        SideMenuOnClick(sideMenuMapBtn);
        SideMenuOnClick(sideMenuFoodBtn);
        SideMenuOnClick(sideMenuMealsBtn);
        SideMenuOnClick(sideMenuShiftsBtn);

        this.getItems().addAll(title, sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn);
    }

    public int getActiveScene() {

        return activeScene;
    }

    public boolean isOnSecondaryScreen() {

        return rootMenu.getLeft().equals(secondaryMenu);
    }

    public void swapMenues() {

        if (isOnSecondaryScreen()) {
            rootMenu.setLeft(this);
        } else {
            rootMenu.setLeft(secondaryMenu);
        }
    }

    private void setUpSecondaryMenu() {

        secondaryMenu = new ToolBar();
        secondaryMenu.setOrientation(Orientation.VERTICAL);
        secondaryMenu.setMaxWidth(Values.sideMenuWidth);
        secondaryMenu.setPrefWidth(Values.sideMenuWidth);
        secondaryMenu.setMinWidth(Values.sideMenuWidth);
        secondaryMenu.setMaxHeight(Values.sideMenuHeight);
        secondaryMenu.setStyle(Styles.secondaryMenu);

        Button start, save, results, quit;
        start = new Button("Start");
        save = new Button("Save");
        results = new Button("Results");
        quit = new Button("Quit");
        Button[] buttons = {start, save, results, quit};

        for (Button b : buttons) {

            b.setMaxSize(Values.sideMenuBtnWidth, Values.sideMenuBtnHeight);
            b.setStyle(Styles.sideMenuBtnActive);

            b.styleProperty().bind(
                    Bindings.when(b.hoverProperty())
                            .then(
                                    new SimpleStringProperty(Styles.secondaryMenuBtnHover)
                            )
                            .otherwise(
                                    new SimpleStringProperty(Styles.secondaryMenuBtn)
                            )
            );

            start.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {

                    Alert programRunningWindow = new Alert(AlertType.NONE);
                    programRunningWindow.setTitle("Simulation Running");
                    programRunningWindow.setHeaderText("Simulation Running");
                    programRunningWindow.setContentText("Please wait while simulation finishes");
                    programRunningWindow.showAndWait();
                }
            });
            // Implement Save Button
            // Implement Results Button

            if (b.equals(quit)) {

                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {

                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        }

        VBox emptySpace = new VBox();
        VBox.setVgrow(emptySpace, Priority.ALWAYS);
        secondaryMenu.getItems().addAll(start, save, results, emptySpace, quit);

        for (Pane p : pages) {

            p.setOnMouseClicked(evt -> {

                if (this.isOnSecondaryScreen()) {

                    this.swapMenues();
                }
            });
        }
    }

    private void HamburgerButtonSetup(Button b) {

        b.styleProperty().bind(
                Bindings.when(b.hoverProperty())
                        .then(
                                new SimpleStringProperty(Styles.sideMenuTitleBtnHover)
                        )
                        .otherwise(
                                new SimpleStringProperty(Styles.sideMenuTitleBtn)
                        )
        );

        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                swapMenues();
            }
        });
    }

    private void SideMenuOnHover(Button b) {

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

    private void SideMenuOnClick(Button b) {

        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                activeScene = menues.get(b);

                if (activeScene.equals(Values.mealsMenuID)) {

                    ((MealsPage)(pages[activeScene-1])).setFoodFrame(
                            ((FoodPage)(pages[Values.foodMenuID-1]))
                    );
                }

                rootMenu.setCenter(pages[activeScene-1]);
                for (Button btn : menues.keySet()) {

                    btn.styleProperty().unbind();
                    SideMenuOnHover(btn);
                }
            }
        });
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
