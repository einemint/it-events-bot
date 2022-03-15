import Services.GetEventsService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class CommandHandler {
    private final String PREFIX_FOR_COMMAND = "/";
    private final String DELIMITER_COMMAND_BOTNAME = "@";
    private final String ERROR_MESSAGE_TEXT = "I don't know this command! Мне неизвестна эта команда!";
    private Bot bot;
    private String botName;

    private GetEventsService getEventsService = new GetEventsService();

    private GetEvents getEvents = new GetEvents(getEventsService);
    private Help help = new Help();
    private Settings settings = new Settings(getEventsService);
    private Start start = new Start();

    public CommandHandler(Bot bot, String botName) {
        this.bot = bot;
        this.botName = botName;
    }

    private boolean isCommand(String text) {
        if (text.startsWith(PREFIX_FOR_COMMAND)) return true;
        else return false;
    }

    private boolean isCommandForBot(String text) {
        if (text.contains(DELIMITER_COMMAND_BOTNAME)) {
            String botNameForEqual = text.substring(text.indexOf(DELIMITER_COMMAND_BOTNAME) + 1);

            return botName.equals(botNameForEqual);
        }

        return true;
    }

    public void handleMessage(String text, Long chatId, Bot bot) {
        text = text.trim();

        if (!text.isEmpty() && !isCommand(text)) {
            if (settings.executeCommand(text, chatId, bot)) { getEvents.executeCommand(chatId, bot); }
        }
        else if (!text.isEmpty() && isCommand(text) && isCommandForBot(text)) {
            switch (text) {
                case "/getevents":
                    getEvents.executeCommand(chatId, bot);
                    break;

                case "/help":
                    help.executeCommand(chatId, bot);
                    break;

                case "/settings":
                    settings.showSettingsMessage(chatId, bot);
                    break;

                case "/start":
                    start.executeCommand(chatId, bot);
                    break;

                default:
                    sendErrorMessage(chatId);
            }
        }
    }

    private void sendErrorMessage(Long chatId) {
        SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(ERROR_MESSAGE_TEXT).build();
        bot.sendQueue.add(sendMessage);
    }
}
