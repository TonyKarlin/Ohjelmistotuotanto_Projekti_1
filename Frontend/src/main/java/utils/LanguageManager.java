package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private LanguageManager() {
        // hidden constructor
    }

    private static Locale currentLocale = Locale.US;
    private static ResourceBundle bundle = null;
    private static String languageBundle = "localization.LanguageBundle";

    private static final List<String> RTL_LANGUAGES = Arrays.asList("ja");

    /**
     * Gets the resource bundle, initializing it if needed
     */
    private static ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(languageBundle, currentLocale);
        }
        return bundle;
    }

    /**
     * Gets a localized string for the given key
     *
     * @param key The resource bundle key
     * @return The localized string
     */
    public static String getString(String key) {
        try {
            return getBundle().getString(key);
        } catch (Exception e) {
            return key; // Return the key itself if translation is not found
        }
    }

    /**
     * Changes the application locale
     *
     * @param locale The new locale to use
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle(languageBundle, currentLocale);
    }

    /**
     * Gets the current locale
     *
     * @return The current locale
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Reloads the resource bundle (useful after locale change)
     */
    public static void reload() {
        bundle = ResourceBundle.getBundle(languageBundle, currentLocale);
    }

    /**
     * Checks if the current locale requires right-to-left orientation
     *
     * @return true if RTL is needed, false otherwise
     */
    public static boolean isRTL() {
        return RTL_LANGUAGES.contains(currentLocale.getLanguage());
    }
}
