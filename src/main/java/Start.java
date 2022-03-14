import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Start {
    private final String START_TEXT = "Добрый день!\n"
            + "Вас приветствует чат-бот, который расскажет обо всех предстоящих событиях в мире IT.\n"
            + "Список моих команд:\n"
            + "/start - Вы сейчас здесь;\n"
            + "/help - получить список команд;\n"
            + "/getevents - получить список ближайших мероприятий с информацией (по умолчанию показываю 20 мероприятий);\n"
            + "/settings - настроить количество показываемых мероприятий и/или добавить ключевое слово"
            + " - тогда я покажу только мероприятия, в названии которых есть ключевое слово, к примеру, \"Java\";\n"
            + "/cat - get cat!";

    public void executeCommand(Long chatId, Bot bot) {
        SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(START_TEXT).build();
        bot.sendQueue.add(sendMessage);
    }
}
