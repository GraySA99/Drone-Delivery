package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Main extends Application {

    private final Styles myStyles = new Styles();
    private final Values myVals = new Values();
    private SideMenu sideMenu;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Setup
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Team Azure Drones");
        BorderPane root = new BorderPane();
        GridPane mapPage = new GridPane();
        BorderPane foodPage = new BorderPane();
        GridPane mealsPage = new GridPane();
        GridPane shiftsPage = new GridPane();
        sideMenu = new SideMenu(new Pane[]{mapPage, foodPage, mealsPage, shiftsPage}, root);

        // DEV ON FOOD PAGE
        foodPage.setStyle(myStyles.foodPage);
        ListView<String> foodList = new ListView<String>();
        foodList.setPrefWidth(550);
        foodList.setStyle(myStyles.foodList);
        foodList.getItems().add("Test");

        Text foodNameLabel = new Text("Name");
        Text foodWeightLabel = new Text("Weight");
        TextField foodNameEnt = new TextField();
        TextField foodWeightEnt = new TextField();

        HBox foodAddItemFrame = new HBox();
        HBox foodAddItemEmptySpaceOne = new HBox();
        HBox foodAddItemEmptySpaceTwo = new HBox();
        HBox.setHgrow(foodAddItemEmptySpaceOne, Priority.ALWAYS);
        HBox.setHgrow(foodAddItemEmptySpaceTwo, Priority.ALWAYS);
        Button foodAddItemBtn = new Button("Add");
        foodAddItemFrame.getChildren().addAll(foodAddItemEmptySpaceOne, foodAddItemBtn, foodAddItemEmptySpaceTwo);

        VBox foodEntryFrame = new VBox();
        VBox.setVgrow(foodEntryFrame, Priority.ALWAYS);
        foodEntryFrame.getChildren().addAll(
                foodNameLabel,
                foodNameEnt,
                foodWeightLabel,
                foodWeightEnt,
                foodAddItemFrame
        );

        foodNameLabel.setStyle(myStyles.foodNameLabel);
        foodWeightLabel.setStyle(myStyles.foodWeightLabel);
        foodEntryFrame.setStyle(myStyles.foodEntryFrame);
        foodNameEnt.setStyle(myStyles.foodNameEnt);
        foodWeightEnt.setStyle(myStyles.foodWeightEnt);

        foodPage.setRight(foodEntryFrame);
        foodPage.setLeft(foodList);
        // Dev Stop

        setOnClickPage(mapPage);
        setOnClickPage(foodPage);
        setOnClickPage(mealsPage);
        setOnClickPage(shiftsPage);

        // Root Setup
        root.setLeft(sideMenu);
        root.setCenter(mapPage);

        // Finalization
        Scene scene = new Scene(root, 600, 300);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setScene(scene);
      
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setOnClickPage(Pane page) {

        page.setOnMouseClicked(evt -> {

            if (sideMenu.isOnSecondaryScreen()) {

                sideMenu.swapMenues();
            }
        });
    }
}
