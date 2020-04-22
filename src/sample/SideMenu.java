package sample;

import Simulation.DataTransfer;
import Simulation.Simulation;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SideMenu extends ToolBar {

    private HashMap<Button, Integer> menues;
    private Pane[] pages;
    private Integer activeScene;
    private ToolBar secondaryMenu;
    private Button sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn;
    private Button start, save, results, quit;
    private HBox title;
    private Text titleLabel;
    private Button titleBtn;

    public SideMenu(Pane[] panes, BorderPane rM, Stage pM) {

        // Set Up
        super();
        menues = new HashMap<Button, Integer>();
        activeScene = 1;
        pages = panes.clone();
        //setUpSecondaryMenu();

        title = new HBox();
        HBox.setHgrow(title, Priority.ALWAYS);
        titleLabel = new Text("AzureSim");
        titleBtn = new Button("|||");
        title.getChildren().addAll(new ESHBox(), titleLabel, new ESHBox(), titleBtn);
        HamburgerButtonSetup(titleBtn);

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
                start, results, save, quit});
        SideMenuOnClick(new Button[]{sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn});

        this.getItems().addAll(title, new Separator(),
                sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn,
                new ESVBox(), new Separator(), new ESVBox(), start, results, save, quit);

        resizeWindow();
    }

    private void SetupActionButtons() {

        start = new Button("Start");
        save = new Button("Save");
        results = new Button("Results");
        quit = new Button("Quit");

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

        // Code to handle save action for save button in sidemenu
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();

                //If we want we can set extension filters for text files
                FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(filter);

                //Show save file dialogue
                File file = fileChooser.showSaveDialog(Values.primaryStage);

                if(file != null) {
                    writeTextToFile(getResultsStr(), file);
                }
            }
        });

        results.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Values.primaryStage.getScene().setRoot(new ResultsPage());
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Platform.exit();
                System.exit(0);
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

        title.setPrefWidth(menuWidth);
        title.setPrefHeight(menuHeight * Values.sideMenuTitleFrameHeightPercent);

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
        quit.setPrefWidth(menuWidth * Values.sideMenuBtnWidthPercent);
        quit.setPrefHeight(menuHeight * Values.sideMenuBtnHeightPercent);

        // Buttons
        SideMenuOnHover(new Button[] {sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn,
                                        start, results, save, quit});
        SideMenuOnClick(new Button[] {sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn});
        HamburgerButtonSetup(titleBtn);
    }

    public int getActiveScene() {

        return activeScene;
    }

    public boolean isOnSecondaryScreen() {

        return Values.rootPage.getLeft().equals(secondaryMenu);
    }

    public void swapMenues() {

        if (isOnSecondaryScreen()) {
            Values.rootPage.setLeft(this);
        } else {
            Values.rootPage.setLeft(secondaryMenu);
        }
    }

    private void setUpSecondaryMenu() {

        secondaryMenu = new ToolBar();
        secondaryMenu.setOrientation(Orientation.VERTICAL);
        secondaryMenu.setStyle(Styles.secondaryMenu);

        start = new Button("Start");
        save = new Button("Save");
        results = new Button("Results");
        quit = new Button("Quit");
        Button[] buttons = {start, save, results, quit};

        for (Button b : buttons) {

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
        }
        secondaryMenu.getItems().addAll(start, save, results, new ESHBox(), quit);

        for (Pane p : pages) {

            p.setOnMouseClicked(evt -> {

                if (this.isOnSecondaryScreen()) {

                    this.swapMenues();
                }
            });
        }
    }

    // Pulls the simulation times from Values and returns their string representation
    private String getResultsStr() {
        String ret = "Your Results:\n";
        ret += "FIFO Avg Time: " + Values.simulation.FIFOaverageTime.toString() + "\n";
        ret += "FIFO Worst Time: " + Values.simulation.FIFOworstTime.toString() + "\n\n";
        ret += "KS Avg Time:" + Values.simulation.KSaverageTime.toString() + "\n";
        ret += "KS Worst Time: " + Values.simulation.KSworstTime.toString() + "\n\n";

        // Need some way to get shift information
        //DataTransfer.getNumShifts()  DataTransfer.getShifts()

        return ret;
    }

    //Writes str to file file
    private void writeTextToFile(String str, File file)
    {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(str);
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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

                //swapMenues();
                Colors.swapToDarkTheme();
                Values.resizeWindow();
                Styles.reset();
                resizeWindow();
                ((MapPage)pages[Values.mapMenuID-1]).refresh();
                ((FoodPage)pages[Values.foodMenuID-1]).resizeWindow();
                ((MealsPage)pages[Values.mealsMenuID-1]).resizeWindow();
                ((ShiftsPage)pages[Values.shiftsMenuID-1]).resizeWindow();
            }
        });
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

                    if (activeScene.equals(Values.mealsMenuID)) {

                        ((MealsPage)(pages[activeScene-1])).setFoodFrame();
                    }

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
