import Bot.Bot;
import Commands.Settings;
import Service.GetEventsService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class SettingsTest extends TestCase {
    private Bot bot;
    private GetEventsService getEventsService;
    private Settings settings;

    private String chatId = "6137210768";

    private String correctTextNumber = "10";
    private String correctTextWord = "Java";
    private String correctTextNumberAndWord = "Java 10";

    private String wrongTextNumberAndWord = "Java 10 10";
    private String wrongEmptyText = "";
    private String wrongTextNumberAndWordWithoutSpace = "Java10";

    public void setUp() throws Exception{
        bot = new Bot();
        getEventsService = new GetEventsService();
        settings = new Settings(getEventsService);
    }

    @Test
    @DisplayName("Некорректная обработка текста командой /settings")
    public void testExecuteCommand() {
        boolean correctTextNumberResult = settings.executeCommand(correctTextNumber, Long.parseLong(chatId), bot);
        boolean correctTextWordResult = settings.executeCommand(correctTextWord, Long.parseLong(chatId), bot);
        boolean correctTextNumberAndWordResult = settings.executeCommand(correctTextNumberAndWord, Long.parseLong(chatId), bot);

        boolean wrongTextNumberAndWordResult = settings.executeCommand(wrongTextNumberAndWord, Long.parseLong(chatId), bot);
        boolean wrongEmptyTextResult = settings.executeCommand(wrongEmptyText, Long.parseLong(chatId), bot);
        boolean wrongTextNumberAndWordWithoutSpaceResult = settings.executeCommand(wrongTextNumberAndWordWithoutSpace, Long.parseLong(chatId), bot);

        assertEquals(correctTextNumberResult, true);
        assertEquals(correctTextWordResult, true);
        assertEquals(correctTextNumberAndWordResult, true);

        assertEquals(wrongTextNumberAndWordResult, false);
        assertEquals(wrongEmptyTextResult, false);
        assertEquals(wrongTextNumberAndWordWithoutSpaceResult, false);
    }
}
