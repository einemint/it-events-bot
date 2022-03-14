import Services.GetEventsService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings {
    private final String REGEX_WORD = "[A-Za-zА-Яа-яЁё]+";
    private final String REGEX_NUMBER = "[0-9]+";
    private final Pattern PATTERN_WORD = Pattern.compile(REGEX_WORD);
    private final Pattern PATTERN_NUMBER = Pattern.compile(REGEX_NUMBER);
    private GetEventsService getEventsService;

    public Settings(GetEventsService getEventsService) { this.getEventsService = getEventsService; }

    private final String SETTINGS_TEXT_ANSWER = "Пожалуйста, отправьте сообщение в формате \"Java\" без кавычек,"
            + "если Вы желаете задать или изменить ключевое слово;\n"
            + "Если Вы хотите изменить количество отображаемых событий - отправьте сообщение в формате \"10\" без кавычек;\n"
            + "Если Вы хотите изменить и количество отображаемых событий, и задать или изменить ключевое слово "
            + "- отправьте сообщение в формате \"Java 10\" без кавычек";
    private final String SETTINGS_TEXT_ACCESS = "Мои настройки успешно изменены! Попробуйте запросить события и получить их с новыми параметрами";

    public void executeCommand(String text, Long chatId, Bot bot) {
        SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(SETTINGS_TEXT_ACCESS).build();

        if (text.contains(" ")) {
            String[] fragments = text.split(" ");
            Matcher matcherWord = PATTERN_WORD.matcher(fragments[0]);
            Matcher matcherNumber = PATTERN_NUMBER.matcher(fragments[1]);

            if (matcherWord.matches() && matcherNumber.matches()) {
                getEventsService.setKeyWord(fragments[0]);
                getEventsService.setEventsQuantity(Integer.parseInt(fragments[1]));
            }
        }
        Matcher matcherWord = PATTERN_WORD.matcher(text);
        Matcher matcherNumber = PATTERN_NUMBER.matcher(text);
        if (matcherWord.matches()) {
            getEventsService.setKeyWord(text);
        }
        if (matcherNumber.matches()) {
            getEventsService.setEventsQuantity(Integer.parseInt(text));
        }
        else sendMessage.setText("Wrong parameters! Неверные параметры!");

        bot.sendQueue.add(sendMessage);
    }

    public void showSettingsMessage(Long chatId, Bot bot) {
        SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(SETTINGS_TEXT_ANSWER).build();
        bot.sendQueue.add(sendMessage);
    }
}
