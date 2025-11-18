package controller.component;

import java.util.Locale;
import java.util.logging.Logger;

import callback.LanguageChangeCallback;
import controller.ConversationSettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import utils.LanguageManager;

/**
 * Controller for the language selection component. Allows users to switch
 * between different application languages.
 */
public class LanguageButtonController {

    private static final Logger logger = Logger.getLogger(LanguageButtonController.class.getName());

    @FXML
    private ComboBox<String> languageComboBox;

    private LanguageChangeCallback languageChangeCallback;

    /**
     * Sets the callback for language change events
     *
     * @param callback The callback to be notified when language changes
     */
    public void setLanguageChangeCallback(LanguageChangeCallback callback) {
        this.languageChangeCallback = callback;
    }

    /**
     * Represents a language option with display name and locale
     */
    private static class LanguageOption {

        private final String displayName;
        private final Locale locale;

        public LanguageOption(String displayName, Locale locale) {
            this.displayName = displayName;
            this.locale = locale;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Locale getLocale() {
            return locale;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    // Available languages
    private final ObservableList<LanguageOption> languages = FXCollections.observableArrayList(
            new LanguageOption("English (US)", Locale.US),
            new LanguageOption("Suomi (Finnish)", Locale.forLanguageTag("fi-FI")),
            new LanguageOption("Русский (Russian)", Locale.forLanguageTag("ru-RU")),
            new LanguageOption("Svenska (Swedish)", Locale.forLanguageTag("sv-SE")),
            new LanguageOption("日本語 (Japanese)", Locale.forLanguageTag("ja-JP"))
    );

    /**
     * Initializes the controller. Sets up the language ComboBox with available
     * languages.
     */
    @FXML
    public void initialize() {
        // Convert LanguageOption list to String list for display
        ObservableList<String> languageNames = FXCollections.observableArrayList();
        for (LanguageOption lang : languages) {
            languageNames.add(lang.getDisplayName());
        }

        languageComboBox.setItems(languageNames);

        // Set the current language as selected
        Locale currentLocale = LanguageManager.getCurrentLocale();
        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).getLocale().equals(currentLocale)) {
                languageComboBox.getSelectionModel().select(i);
                break;
            }
        }

        // Add listener for language change
        languageComboBox.setOnAction(event -> handleLanguageChange());
    }

    /**
     * Handles language change event. Updates the application locale when user
     * selects a different language.
     */
    private void handleLanguageChange() {
        int selectedIndex = languageComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            LanguageOption selectedLanguage = languages.get(selectedIndex);
            Locale newLocale = selectedLanguage.getLocale();

            // Update the language manager
            LanguageManager.setLocale(newLocale);

            // Notify the callback if set
            if (languageChangeCallback != null) {
                languageChangeCallback.onLanguageChanged(newLocale);
            }

            logger.info("Language changed to: " + selectedLanguage.getDisplayName());
        }
    }

    /**
     * Gets the currently selected locale
     *
     * @return The selected Locale
     */
    public Locale getSelectedLocale() {
        int selectedIndex = languageComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            return languages.get(selectedIndex).getLocale();
        }
        return LanguageManager.getCurrentLocale();
    }
}
