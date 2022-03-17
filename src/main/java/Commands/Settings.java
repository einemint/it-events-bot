package Commands;

import Bot.Bot;
import Service.GetEventsService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings {
    private final String REGEX_WORD = "[A-Za-zА-Яа-яЁё]+";
    private final String REGEX_NUMBER = "[0-9]+";
    private final Pattern PATTERN_WORD = Pattern.compile(REGEX_WORD);
    private final Pattern PATTERN_NUMBER = Pattern.compile(REGEX_NUMBER);
    private final String SETTINGS_TEXT_ACCESS = "Мои настройки успешно изменены! Список событий с новыми параметрами: ";
    private GetEventsService getEventsService;

    public Settings(GetEventsService getEventsService) { this.getEventsService = getEventsService; }

    public boolean executeCommand(String text, Long chatId, Bot bot) {
        SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(SETTINGS_TEXT_ACCESS).build();

        if (text.contains(" ")) {
            String[] fragments = text.split(" ");
            Matcher matcherWord = PATTERN_WORD.matcher(fragments[0]);
            Matcher matcherNumber = PATTERN_NUMBER.matcher(fragments[1]);

            if (matcherWord.matches() && matcherNumber.matches() && fragments.length == 2) {
                getEventsService.setKeyWord(fragments[0]);
                getEventsService.setEventsQuantity(Integer.parseInt(fragments[1]));
            }
            else {
                sendMessage.setText("Неверные параметры!");
                return false;
            }
        }

        else {
            Matcher matcherWord = PATTERN_WORD.matcher(text);
            Matcher matcherNumber = PATTERN_NUMBER.matcher(text);

            if (matcherWord.matches()) {
                getEventsService.setKeyWord(text);
            }
            else if (matcherNumber.matches()) {
                getEventsService.setEventsQuantity(Integer.parseInt(text));
            } else {
                sendMessage.setText("Неверные параметры!");
                return false;
            }
        }

        bot.sendQueue.add(sendMessage);
        return true;
    }

    public void showSettingsMessage(Long chatId, Bot bot) {
        String settingsTextAnswer;
        if (getEventsService.getKeyWord().isEmpty()) {
            settingsTextAnswer = "Пожалуйста, отправьте сообщение в формате \"Java\" без кавычек, "
                    + "если Вы желаете задать или изменить искомый язык программирования;\n\n"
                    + "Если Вы хотите изменить количество отображаемых событий - отправьте сообщение в формате \"10\" без кавычек;\n\n"
                    + "Если Вы хотите изменить и количество отображаемых событий, и задать или изменить язык программирования "
                    + "- отправьте сообщение в формате \"Java 10\" без кавычек\n\n"
                    + "Текущее искомое слово отсутствует;\n"
                    + "Текущее количество выводимых результатов: " + getEventsService.getEventsQuantity() + "\n\n"
                    + "Для сброса настроек наберите \"Сброс\" без кавычек";
        }
        else {
            settingsTextAnswer = "Пожалуйста, отправьте сообщение в формате \"Java\" без кавычек, "
                    + "если Вы желаете задать или изменить искомый язык программирования;\n\n"
                    + "Если Вы хотите изменить количество отображаемых событий - отправьте сообщение в формате \"10\" без кавычек;\n\n"
                    + "Если Вы хотите изменить и количество отображаемых событий, и задать или изменить язык программирования "
                    + "- отправьте сообщение в формате \"Java 10\" без кавычек\n\n"
                    + "Текущее искомое слово: " + getEventsService.getKeyWord() + ";\n"
                    + "Текущее количество выводимых результатов: " + getEventsService.getEventsQuantity() + "\n\n"
                    + "Для сброса настроек наберите \"Сброс\" без кавычек";
        }

        SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(settingsTextAnswer).build();
        bot.sendQueue.add(sendMessage);
    }
}
