package sample;

import Mapping.Waypoint;
import Simulation.DataTransfer;
import Simulation.Simulation;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class SideMenu extends ToolBar {

    private HashMap<Button, Integer> menues;
    private Pane[] pages;
    private Integer activeScene;
    private ToolBar secondaryMenu;

    public SideMenu(Pane[] panes, BorderPane rM, Stage pM) {

        // Set Up
        super();
        menues = new HashMap<Button, Integer>();
        activeScene = 1;
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
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setMaxSize(Values.sideMenuBtnWidth, Values.sideMenuBtnHeight);
        Text titleLabel = new Text("AzureSim");
        Button titleBtn = new Button("|||");
        title.getChildren().addAll(new ESHBox(), titleLabel, new ESHBox(), titleBtn);
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

        SideMenuOnHover(new Button[]{sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn});
        SideMenuOnClick(new Button[]{sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn});

        this.getItems().addAll(title, sideMenuMapBtn, sideMenuFoodBtn, sideMenuMealsBtn, sideMenuShiftsBtn);
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

//                    final Stage dialog = new Stage();
//                    dialog.initModality(Modality.APPLICATION_MODAL);
//                    //dialog.initOwner(rootMenu);
//
//                    BorderPane subRoot = new BorderPane();
//                    Text dialogText = new Text("Simulation is Running Please Wait");
//                    subRoot.setCenter(dialogText);
//                    Scene dialogScene = new Scene(subRoot, 300, 200);
//                    dialog.setScene(dialogScene);
//                    dialog.show();
//                    //dialog.close();

                    Values.simulation = new Simulation();
                    Values.simulation.runSimulation();
                }
            });

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
                    swapMenues();
                }
            });

            if (b.equals(quit)) {

                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {

                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
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

    private String getResultsStr() {
        String ret = "Your Results:\n";
        // Need to actually get the simulation data
        ret += "";
        DataTransfer.getShifts();

        //DataTransfer.getNumShifts()  DataTransfer.getShifts()

        return ret;
    }

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

                swapMenues();
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
