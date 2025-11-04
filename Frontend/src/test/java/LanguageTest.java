import org.junit.jupiter.api.Test;
import utils.LanguageManager;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class LanguageTest {

    LanguageManager lm;

    public LanguageTest() {
        lm = new LanguageManager();
    }

    @Test
    public void testEnglishBundle() {
        Locale locale = Locale.US;
        LanguageManager.setLocale(locale);
        String message = LanguageManager.getString("give_conversation_name");
        assertEquals("Give conversation a name#", message);
    }

    @Test
    public  void testFinnishBundle() {
        Locale locale = new Locale("fi", "FI");
        LanguageManager.setLocale(locale);
        String message = LanguageManager.getString("give_conversation_name");
        assertEquals("Anna keskustelulle nimi", message);
    }

    @Test
    public void testSwedishBundle() {
        Locale locale = new Locale("sv", "SE");
        LanguageManager.setLocale(locale);
        String message = LanguageManager.getString("give_conversation_name");
        assertEquals("Ge konversationen ett namn", message);
    }

    @Test
    public void testJapaneseBundle() {
        Locale locale = new Locale("ja", "JP");
        LanguageManager.setLocale(locale);
        String message = LanguageManager.getString("give_conversation_name");
        assertEquals("会話に名前を付けてください", message);
    }

    @Test
    public void testRussianBundle() {
        Locale locale = new Locale("ru", "RU");
        LanguageManager.setLocale(locale);
        String message = LanguageManager.getString("give_conversation_name");
        assertEquals("Дайте беседе название", message);
    }

}
