package utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages language/locale for the application. Provides centralized access to
 * localized strings.
 */
public class LanguageManager {

    private static Locale currentLocale = Locale.US;
    private static ResourceBundle bundle = null;

    /**
     * Gets the resource bundle, initializing it if needed
     */
    private static ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("localization.LanguageBundle", currentLocale);
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
        bundle = ResourceBundle.getBundle("localization.LanguageBundle", currentLocale);
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
        bundle = ResourceBundle.getBundle("localization.LanguageBundle", currentLocale);
    }
}
