package sample;

import Simulation.Simulation;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Values {

    public static final String defaultShiftsFileName = "DefaultShifts.txt";
    public static final String defaultMealsFileName = "DefaultMeals.txt";
    public static final String defaultFoodFileName = "DefaultFoodItems.txt";
    public static final String defaultWaypointFileName = "DefaultWaypoints.txt";

    public static Simulation simulation;

    public static Stage primaryStage;
    public static BorderPane rootPage;

    public static double windowHeight = 0;
    public static double windowWidth = 0;
    public static int sideMenuFontSize = 0;

    public static void resizeWindow() {

        windowHeight = primaryStage.getHeight();
        windowWidth = primaryStage.getWidth();
        sideMenuFontSize = (int) (windowHeight * 0.03);
    }

    public static final int numberOfMenues = 4;
    public static final Integer mapMenuID = 1;
    public static final Integer foodMenuID = 2;
    public static final Integer mealsMenuID = 3;
    public static final Integer shiftsMenuID = 4;

    // Map Page
    public static final double mapPageListWidthPercent = 0.45;
    public static final double mapPageListHeightPercent = 0.85;
    public static final double mapPageMapWidthPercent = 0.4;
    public static final double mapPageMapHeightPercent = 0.65;
    public static final double mapPageMapInfoWidthPercent = 0.45;
    public static final double mapPageMapInfoHeightPercent = 0.25;
    public static final double mapPageBtnWidthPercent = 0.25;
    public static final double mapPageBtnHeightPercent = 0.15;
    public static final double mapPageNameWidthPercent = 0.65;
    public static final double mapPageNameHeightPercent = 0.33;
    public static final double mapPageNameFontPercent = 0.14;
    public static final double mapPageLatLngFontPercent = 0.18;
    public static final double mapPageDPListWidthPercent = 0.35;
    public static final double mapPageDPListHeightPercent = 0.85;

    // Side Menu
    public static final double sideMenuWidthPercent = 0.10;
    public static final double mainPageWidthPercent = 0.85;
    public static final double sideMenuBtnWidthPercent = 1.0;
    public static final double sideMenuBtnHeightPercent = 0.09;
    public static final int sideMenuTitleFontSize = 22;
    public static final double sideMenuTitleFrameHeightPercent = 0.05;

    public static final String googleMapsAPIKey = "AIzaSyCC1c7VEIHPU08i-fNQEkfps0S4i-TsU9I";

}
