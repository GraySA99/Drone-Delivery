package GUI;

import Simulation.Simulation;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Values {

    public static MapPage mapPage;
    public static FoodPage foodPage;
    public static MealsPage mealsPage;
    public static ShiftsPage shiftsPage;

    public static final String defaultFileName = "defaults_universal.dd";
    //public static final String defaultShiftsFileName = "DefaultShifts.txt";
    //public static final String defaultMealsFileName = "DefaultMeals.txt";
    //public static final String defaultFoodFileName = "DefaultFoodItems.txt";
    //public static final String defaultWaypointFileName = "DefaultWaypoints.txt";

    public static Simulation simulation;

    public static Stage primaryStage;
    public static BorderPane rootPage;
    public static Main main;

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

    // Food Page
    public static final double foodPageFoodListWidthPercent = 0.45;
    public static final double foodPageFoodListHeightPercent = 0.85;
    public static final double foodPageFoodItemEntryWidthPercent = 0.35;
    public static final double foodPageFoodItemEntryHeightPercent = 0.85;
    public static final double foodPageFoodNameEntWidthPercent = 0.75;
    public static final double foodPageFoodNameEntHeightPercent = 0.05;
    public static final double foodPageFoodWeightEntWidthPercent = 0.75;
    public static final double foodPageFoodWeightEntHeightPercent = 0.05;
    public static final double foodPageBtnWidthPercent = 0.15;
    public static final double foodPageBtnHeightPercent = 0.04;
    public static final double foodPageFontSize = 0.50;

    // Meals Page
    public static final double mealsPageListWidthPercent = 0.35;
    public static final double mealsPageListHeightPercent = 0.75;
    public static final double mealsPageMealEntryWidthPercent = 0.45;
    public static final double mealsPageMealEntryHeightPercent = 0.85;
    public static final double mealsPageMealsLabelFontPercent = 0.07;
    public static final double mealsPageBtnWidthPercent = 0.25;
    public static final double mealsPageBtnHeightPercent = 0.15;
    public static final double mealsPageNameFrameWidthPercent = 0.90;
    public static final double mealsPageNameFrameHeightPercent = 0.15;
    public static final double mealsPageProbFrameWidthPercent = 0.90;
    public static final double mealsPageProbFrameHeightPercent = 0.15;
    public static final double mealsPageFoodScrollFrameWidthPercent = 0.90;
    public static final double mealsPageFoodScrollFrameHeightPercent = 0.60;
    public static final double mealsPageBtnFrameWidthPercent = 0.90;
    public static final double mealsPageBtnFrameHeightPercent = 0.15;
    public static final double mealsPageFoodFrameWidthPercent = 1.0;
    public static final double mealsPageFoodFrameHeightPercent = 1.0;
    public static final double mealsPageFoodItemWidthPercent = 1.0;
    public static final double mealsPageFoodItemHeightPercent = 0.25;

    // Map Page
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
    public static final double mapPageDPListHeightPercent = 0.75;

    // Side Menu
    public static final double sideMenuWidthPercent = 0.10;
    public static final double mainPageWidthPercent = 0.85;
    public static final double sideMenuBtnWidthPercent = 1.0;
    public static final double sideMenuBtnHeightPercent = 0.09;
    public static final int sideMenuTitleFontSize = 22;
    public static final double sideMenuTitleFrameHeightPercent = 0.05;

    public static final String googleMapsAPIKey = "AIzaSyCC1c7VEIHPU08i-fNQEkfps0S4i-TsU9I";

}
