import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Help {
    private final String HELP_TEXT = "Список моих команд:\n"
            + "/start - Вы сейчас здесь;\n"
            + "/help - получить список команд;\n"
            + "/getevents - получить список ближайших мероприятий с информацией (по умолчанию показываю 20 мероприятий);\n"
            + "/settings - настроить количество показываемых мероприятий и/или добавить поиск по названию языка программирования"
            + " - тогда я покажу только мероприятия, в названии которых есть заданный язык, к примеру, \"Java\"";

    public void executeCommand(Long chatId, Bot bot) {
        SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(HELP_TEXT).build();
        bot.sendQueue.add(sendMessage);
    }
}
