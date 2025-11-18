package fronttest;

import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import utils.LanguageManager;

class LanguageTest {

    @SuppressWarnings("unused")
    static Stream<Arguments> languageBundleProvider() {
        return Stream.of(
                Arguments.of(Locale.US, "Give conversation a name"),
                Arguments.of(Locale.of("fi", "FI"), "Anna keskustelulle nimi"),
                Arguments.of(Locale.of("sv", "SE"), "Ge konversationen ett namn"),
                Arguments.of(Locale.of("ja", "JP"), "会話に名前を付けてください"),
                Arguments.of(Locale.of("ru", "RU"), "Дайте беседе название")
        );
    }

    @ParameterizedTest
    @MethodSource("languageBundleProvider")
    void testLanguageBundle(Locale locale, String expectedMessage) {
        LanguageManager.setLocale(locale);
        String message = LanguageManager.getString("give_conversation_name");
        assertEquals(expectedMessage, message);
    }

}
