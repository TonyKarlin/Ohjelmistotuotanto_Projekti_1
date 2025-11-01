package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private static Locale currentLocale = Locale.of("en", "US");
    private static ResourceBundle bundle;

    static {
        bundle = ResourceBundle.getBundle("localization.LanguageBundle", currentLocale);
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("localization.LanguageBundle", locale);
    }

    public static void setLocale(String language, String country) {
        setLocale(Locale.of(language, country));
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static String getString(String key, String defaultValue) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
