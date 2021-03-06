package GUI;

public class Styles {

    // Results Page
    public static String resultsDataFrame = ""
            + "-fx-background-color: " + Colors.secondaryColor + ";\n";
    public static String resultsDeliveryTime = ""
            + "-fx-font-size:50px;\n";
    public static String resultsDeliveryTypeLabel = "";
    public static String FIFODataFrame = "";
    public static String resultsButtonFrame = "";
    public static String resultsBarChart = "-fx-padding: 10px 50px 50px 10px;";
    public static String resultsPageBackground = "-fx-background-color: " + Colors.secondaryColor + ";\n";

    // Map Page
    public static String mapPageCurrentPointLabel = "";
    public static String mapPageNameLabel = "";
    public static String mapPageMapView = ""
            + "-fx-border-style: solid;\n";
    public static String mapPageMapInfoContainer = "";
    public static String mapPageDPListContainer = ""
            + "-fx-padding: 0px 60px 0px 0px;\n";
    public static String mapPageMapContainer = ""
            + "-fx-padding: 50px 0px 50px 50px;\n";
    public static String mapPage = ""
            + "-fx-background-color: " + Colors.secondaryColor + ";\n";

    // Food Page
    public static String foodPageFoodListContainer = ""
            + "-fx-padding: 0px 50px 0px 0px;\n";
    public static String foodPageFoodLabel = "";
    public static String foodPageWeightLabel = "";
    public static String foodPage = ""
            + "fx-background-color: " + Colors.secondaryColor + ";\n";

    // Meals Page
    public static String mealsPage = ""
            + "-fx-background-color: " + Colors.secondaryColor + ";\n";
    public static String mealsPageMealsListContainer = ""
            + "-fx-padding: 50px 50px 50px 50px;\n";
    public static String mealsPageMealsNameLabel = "";
    public static String mealsPageMealsProbLabel = "";
    public static String mealsPageMealEntry = ""
            + "-fx-padding: 50px 0px 0px 50px;\n";
    public static String mealsPageFoodItem = ""
            + "-fx-border-style: hidden hidden solid hidden;\n";

    // Menu
    public static String secondaryMenuBtn = ""
            + "-fx-background-color: " + Colors.primaryColor + ";\n";
    public static String secondaryMenuBtnHover = ""
            + "-fx-background-color: " + Colors.quaternaryColor + ";\n";
    public static String secondaryMenu = ""
            + "-fx-background-color: " + Colors.primaryColor + ";\n";
    public static String sideMenuTitleBtn = ""
            + "-fx-background-color: " + Colors.primaryColor + ";\n"
            + "-fx-text-fill: " + Colors.contrastFontColor + ";\n";
    public static String sideMenuTitleBtnHover = ""
            + "-fx-text-fill: " + Colors.quaternaryColor + ";\n"
            + "-fx-background-color: " + Colors.primaryColor + ";\n";
    public static String sideMenuTitle = ""
            + "-fx-background-color: " + Colors.primaryColor + ";\n";
    public static String sideMenuTitleText = ""
            + "-fx-background-color: " + Colors.primaryColor + ";\n"
            + "-fx-fill: " + Colors.contrastFontColor + ";\n"
            + "-fx-font-size: " + Values.sideMenuTitleFontSize + ";\n";
    public static String sideMenu = ""
            + "-fx-background-color: " + Colors.primaryColor + ";\n";
    public static String sideMenuBtn = ""
            + "-fx-background-color: " + Colors.primaryColor + ";\n"
            + "-fx-color: " + Colors.complimentaryFontColor + ";\n"
            + "-fx-font-size: " + Values.sideMenuFontSize + "px;\n";
    public static String sideMenuBtnActive = ""
            + "-fx-background-color: " + Colors.tertiaryColor + ";\n"
            + "-fx-color: " + Colors.contrastFontColor + ";\n"
            + "-fx-font-size: " + Values.sideMenuFontSize + "px;\n";
    public static String sideMenuBtnHover = ""
            + "-fx-background-color: " + Colors.quaternaryColor + ";\n"
            + "-fx-color: " + Colors.complimentaryFontColor + ";\n"
            + "-fx-font-size: " + Values.sideMenuFontSize + "px;\n";

    // Page Title
    public static String pageTitleLabelContainer = "";
    public static String pageTitleLabel = "";
    public static String pageTitle = "";

    public static void reset() {

        resultsDataFrame = "";
        resultsDeliveryTime = "";
        resultsDeliveryTypeLabel = "";
        FIFODataFrame = "";
        resultsButtonFrame = "";

        // Food Page
        foodPageFoodListContainer = ""
                + "-fx-padding: 0px 50px 0px 0px;\n";
        foodPage = ""
                + "fx-background-color: " + Colors.secondaryColor + ";\n";

        // Map Page
        mapPageCurrentPointLabel = "";
        mapPageNameLabel = "";
        mapPageMapView = ""
                + "-fx-border-style: solid;\n";
        mapPageMapInfoContainer = "";
        mapPageDPListContainer = ""
                + "-fx-padding: 0px 60px 0px 0px;\n";
        mapPageMapContainer = ""
                + "-fx-padding: 50px 0px 50px 50px;\n";
        mapPage = ""
                + "-fx-background-color: " + Colors.secondaryColor + ";\n";

        // Meals Page
        mealsPage = ""
                + "-fx-background-color: " + Colors.secondaryColor + ";\n";
        mealsPageMealsListContainer = ""
                + "-fx-padding: 50px 50px 50px 50px;\n";
        mealsPageMealsNameLabel = "";
        mealsPageMealsProbLabel = "";
        mealsPageMealEntry = ""
                + "-fx-padding: 50px 0px 0px 50px;\n";
        mealsPageFoodItem = ""
                + "-fx-border-style: hidden hidden solid hidden;\n";

        // Side Menu
        secondaryMenuBtn = ""
                + "-fx-background-color: " + Colors.primaryColor + ";\n";
        secondaryMenuBtnHover = ""
                + "-fx-background-color: " + Colors.quaternaryColor + ";\n";
        secondaryMenu = ""
                + "-fx-background-color: " + Colors.primaryColor + ";\n";
        sideMenuTitleBtn = ""
                + "-fx-background-color: " + Colors.primaryColor + ";\n"
                + "-fx-text-fill: " + Colors.contrastFontColor + ";\n";
        sideMenuTitleBtnHover = ""
                + "-fx-text-fill: " + Colors.quaternaryColor + ";\n"
                + "-fx-background-color: " + Colors.primaryColor + ";\n";
        sideMenuTitle = ""
                + "-fx-background-color: " + Colors.primaryColor + ";\n";
        sideMenuTitleText = ""
                + "-fx-background-color: " + Colors.primaryColor + ";\n"
                + "-fx-fill: " + Colors.contrastFontColor + ";\n"
                + "-fx-font-size: " + Values.sideMenuTitleFontSize + ";\n"
                + "-fx-font-weight: bold;\n";
        sideMenu = ""
                + "-fx-background-color: " + Colors.primaryColor + ";\n"
                + "-fx-border-style: hidden solid hidden hidden;\n";
        sideMenuBtn = ""
                + "-fx-background-color: " + Colors.primaryColor + ";\n"
                + "-fx-color: " + Colors.complimentaryFontColor + ";\n"
                + "-fx-font-size: " + Values.sideMenuFontSize + "px;\n";
        sideMenuBtnActive = ""
                + "-fx-background-color: " + Colors.tertiaryColor + ";\n"
                + "-fx-color: " + Colors.contrastFontColor + ";\n"
                + "-fx-font-size: " + Values.sideMenuFontSize + "px;\n";
        sideMenuBtnHover = ""
                + "-fx-background-color: " + Colors.quaternaryColor + ";\n"
                + "-fx-color: " + Colors.complimentaryFontColor + ";\n"
                + "-fx-font-size: " + Values.sideMenuFontSize + "px;\n";
        pageTitleLabelContainer = "";
        pageTitleLabel = "";
        pageTitle = "";
    }
}
