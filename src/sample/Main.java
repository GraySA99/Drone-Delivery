package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        BorderPane mapPage = new BorderPane();
        BorderPane foodPage = new BorderPane();
        BorderPane mealsPage = new BorderPane();
        BorderPane shiftsPage = new BorderPane();
        sideMenu = new SideMenu(new Pane[]{mapPage, foodPage, mealsPage, shiftsPage}, root);

        Text mapPageTitle = new Text("Map");
        mapPage.setTop(mapPageTitle);

        Text foodPageTitle = new Text("Food");
        foodPage.setTop(foodPageTitle);

        Text mealsPageTitle = new Text("Meals");
        mealsPage.setTop(mealsPageTitle);

        Text shiftsPageTitle = new Text("Shifts");
        shiftsPage.setTop(shiftsPageTitle);

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
