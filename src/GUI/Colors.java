package GUI;

public class Colors {

    private static int themeIndex = 0;

    private static final String[] primary_colors = {
        "#ffffff",
        "#1b262c"
    };

    private static final String[] secondary_colors = {
        "#e3f6f5",
        "#0f4c75"
    };

    private static final String[] tertiary_colors = {
        "#272343",
        "#bbe1fa"
    };

    private static final String[] quaternary_colors = {
        "#bae8e8",
        "#3282b8"
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
    public static void swapToDarkTheme() { themeIndex = 1; changeTheme(); }
    private static void changeTheme() {

        primaryColor = primary_colors[themeIndex];
        secondaryColor = secondary_colors[themeIndex];
        tertiaryColor = tertiary_colors[themeIndex];
        quaternaryColor = quaternary_colors[themeIndex];
        contrastFontColor = contrast_font_colors[themeIndex];
        complimentaryFontColor = complimentary_font_colors[themeIndex];
    }
}
