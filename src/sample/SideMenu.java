package sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class SideMenu extends ToolBar {

    private final Styles myStyles = new Styles();
    private final Values myVals = new Values();
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
        this.setMaxWidth(myVals.sideMenuWidth);
        this.setPrefWidth(myVals.sideMenuWidth);
        this.setMinWidth(myVals.sideMenuWidth);
        this.setMaxHeight(myVals.sideMenuHeight);
        this.setStyle(myStyles.sideMenu);
        setUpSecondaryMenu();

        HBox title = new HBox();
        HBox titleEmptySpace = new HBox();
        HBox titleEmptySpace2 = new HBox();
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(titleEmptySpace, Priority.ALWAYS);
        HBox.setHgrow(titleEmptySpace2, Priority.ALWAYS);
        title.setMaxSize(myVals.sideMenuBtnWidth, myVals.sideMenuBtnHeight);
        Text titleLabel = new Text("AzureSim");
        Button titleBtn = new Button("|||");
        title.getChildren().addAll(titleEmptySpace2, titleLabel, titleEmptySpace, titleBtn);
        title.setStyle(myStyles.sideMenuTitle);
        titleLabel.setStyle(myStyles.sideMenuTitleText);
        HamburgerButtonSetup(titleBtn);

        sideMenuMapBtn = new Button("Map");
        sideMenuMapBtn.setMaxSize(myVals.sideMenuBtnWidth, myVals.sideMenuBtnHeight);
        sideMenuMapBtn.setStyle(myStyles.sideMenuBtnActive);
        sideMenuFoodBtn = new Button("Food");
        sideMenuFoodBtn.setMaxSize(myVals.sideMenuBtnWidth, myVals.sideMenuBtnHeight);
        sideMenuFoodBtn.setStyle(myStyles.sideMenuBtn);
        sideMenuMealsBtn = new Button("Meals");
        sideMenuMealsBtn.setMaxSize(myVals.sideMenuBtnWidth, myVals.sideMenuBtnHeight);
        sideMenuMealsBtn.setStyle(myStyles.sideMenuBtn);
        sideMenuShiftsBtn = new Button("Shifts");
        sideMenuShiftsBtn.setMaxSize(myVals.sideMenuBtnWidth, myVals.sideMenuBtnHeight);
        sideMenuShiftsBtn.setStyle(myStyles.sideMenuBtn);

        menues.put(sideMenuMapBtn, myVals.mapMenuID);
        menues.put(sideMenuFoodBtn, myVals.foodMenuID);
        menues.put(sideMenuMealsBtn, myVals.mealsMenuID);
        menues.put(sideMenuShiftsBtn, myVals.shiftsMenuID);

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
        secondaryMenu.setMaxWidth(myVals.sideMenuWidth);
        secondaryMenu.setPrefWidth(myVals.sideMenuWidth);
        secondaryMenu.setMinWidth(myVals.sideMenuWidth);
        secondaryMenu.setMaxHeight(myVals.sideMenuHeight);
        secondaryMenu.setStyle(myStyles.secondaryMenu);

        Button start, save, results, quit;
        start = new Button("Start");
        save = new Button("Save");
        results = new Button("Results");
        quit = new Button("Quit");
        Button[] buttons = {start, save, results, quit};

        for (Button b : buttons) {

            b.setMaxSize(myVals.sideMenuBtnWidth, myVals.sideMenuBtnHeight);
            b.setStyle(myStyles.sideMenuBtnActive);

            b.styleProperty().bind(
                    Bindings.when(b.hoverProperty())
                            .then(
                                    new SimpleStringProperty(myStyles.secondaryMenuBtnHover)
                            )
                            .otherwise(
                                    new SimpleStringProperty(myStyles.secondaryMenuBtn)
                            )
            );

            // Implement Start Button
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
    }

    private void HamburgerButtonSetup(Button b) {

        b.styleProperty().bind(
                Bindings.when(b.hoverProperty())
                        .then(
                                new SimpleStringProperty(myStyles.sideMenuTitleBtnHover)
                        )
                        .otherwise(
                                new SimpleStringProperty(myStyles.sideMenuTitleBtn)
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

            return myStyles.sideMenuBtnActive;
        }

        return isHover
                ? myStyles.sideMenuBtnHover
                : myStyles.sideMenuBtn;
    }
}
