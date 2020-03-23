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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
    private HashMap<String, HBox> foodItems;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Setup
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Team Azure Drones");
        BorderPane root = new BorderPane();
        MapPage mapPage = new MapPage();
        FoodPage foodPage = new FoodPage();
        MealsPage mealsPage = new MealsPage();
        ShiftsPage shiftsPage = new ShiftsPage();
        sideMenu = new SideMenu(new Pane[]{mapPage, foodPage, mealsPage, shiftsPage}, root);

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

    static { // use system proxy settings when standalone application
        System.setProperty("java.net.useSystemProxies", "true");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
