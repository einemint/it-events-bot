import Services.GetEventsService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;

public class GetEvents {
    private GetEventsService getEventsService;
    private ArrayList<String> events;

    public GetEvents(GetEventsService getEventsService) {
        this.getEventsService = getEventsService;
    }

    public void executeCommand(Long chatId, Bot bot) {
        events = getEventsService.getEvents();
        for (String event : events) {
            SendMessage sendMessage = SendMessage.builder().chatId(Long.toString(chatId)).text(event).build();
            bot.sendQueue.add(sendMessage);
        }
    }
}
