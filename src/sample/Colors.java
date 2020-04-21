package sample;

public class Colors {

    private static int themeIndex = 0;

    private static final String[] primary_colors = {
        "#ffffff"
    };

    private static final String[] secondary_colors = {
        "#e3f6f5"
    };

    private static final String[] tertiary_colors = {
        "#272343"
    };

    private static final String[] quaternary_colors = {
        "#bae8e8"
    };

    private static final String[] contrast_font_colors = {

        "#000000",
        "#FFFFFF"
    };

    private static final String[] complimentary_font_colors = {

        "#FFFFFF",
        "#000000"
    };

    public static String primaryColor = primary_colors[themeIndex];
    public static String secondaryColor = secondary_colors[themeIndex];
    public static String tertiaryColor = tertiary_colors[themeIndex];
    public static String quaternaryColor = quaternary_colors[themeIndex];
    public static String contrastFontColor = contrast_font_colors[themeIndex];
    public static String complimentaryFontColor = complimentary_font_colors[themeIndex];

    public static void swapToLightTheme() { themeIndex = 0; changeTheme(); }
    public static void swapToDarkTheme() { themeIndex = 0; changeTheme(); }
    private static void changeTheme() {

        primaryColor = primary_colors[themeIndex];
        secondaryColor = secondary_colors[themeIndex];
        tertiaryColor = tertiary_colors[themeIndex];
        quaternaryColor = quaternary_colors[themeIndex];
        contrastFontColor = contrast_font_colors[themeIndex];
        complimentaryFontColor = complimentary_font_colors[themeIndex];
    }
}
