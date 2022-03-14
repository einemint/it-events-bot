import Services.GetEventsService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;

public class GetEvents {
    private final String EMPTY_RESPONSE_TEXT = "Увы, мероприятий нет!";
    private GetEventsService getEventsService;
    private ArrayList<String> events;

    public GetEvents(GetEventsService getEventsService) {
        this.getEventsService = getEventsService;
    }

    public void executeCommand(Long chatId, Bot bot) {
        events = getEventsService.getEvents();
        if (events.isEmpty()) {
            SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(EMPTY_RESPONSE_TEXT).build();
            bot.sendQueue.add(sendMessage);
        }
        else {
            for (String event : events) {
                SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(event).build();
                bot.sendQueue.add(sendMessage);
            }
        }
    }
}
