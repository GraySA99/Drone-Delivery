package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    private final Styles myStyles = new Styles();
    private final Values myVals = new Values();
    private SideMenu sideMenu;
    private HashMap<String, HBox> foodItems;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Setup
        //Parent root1 = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Team Azure Drones");
        BorderPane root = new BorderPane();
        MapPage mapPage = new MapPage();
        FoodPage foodPage = new FoodPage();
        MealsPage mealsPage = new MealsPage();
        ShiftsPage shiftsPage = new ShiftsPage();
        sideMenu = new SideMenu(new Pane[]{mapPage, foodPage, mealsPage, shiftsPage}, root, primaryStage);
        Values.primaryStage = primaryStage;
        Values.rootPage = root;

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
