import Bot.Bot;
import Commands.CommandHandler;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class CommandHandlerTest extends TestCase{
    private Bot bot;
    private CommandHandler commandHandler;

    private String botName = "it_events";

    private String correctCommand = "/start";
    private String wrongCommand = "start";

    private String correctCommandWithDelimiter = "/start@it_events";
    private String wrongCommandWithDelimiter = "/start@it";

    public void setUp() throws Exception{
        bot = new Bot();
        commandHandler = new CommandHandler(bot, botName);
    }

    @Test
    @DisplayName("Некорректное определение команд")
    public void testIsCommandAndIsCommandForBot() {
        boolean correctCommandResult = commandHandler.isCommand(correctCommand);
        boolean wrongCommandResult = commandHandler.isCommand(wrongCommand);

        boolean correctCommandWithDelimiterResult = commandHandler.isCommandForBot(correctCommandWithDelimiter);
        boolean wrongCommandWithDelimiterResult = commandHandler.isCommandForBot(wrongCommandWithDelimiter);

        assertEquals(correctCommandResult, true);
        assertEquals(wrongCommandResult, false);

        assertEquals(correctCommandWithDelimiterResult, true);
        assertEquals(wrongCommandWithDelimiterResult, false);
    }
}
