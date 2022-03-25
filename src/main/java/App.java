import Bot.Bot;
import MessageHandlers.MessageReceiver;
import MessageHandlers.MessageSender;
import Service.GetEventsService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Timer;
import java.util.TimerTask;

public class App {
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 2;
    private static final String BOT_ADMIN_ID = "5147810668";
    private final static String BOT_NAME = "it_events_fresher_bot";
    private final static String TOKEN = "5106867496:AAEu6VF0UA-eIADcw8OOL5YeDbIhY-ui7N0";

    public static void main(String[] args) {
        Bot ITEventsBot = new Bot(BOT_NAME, TOKEN);

        MessageReceiver messageReceiver = new MessageReceiver(ITEventsBot);
        MessageSender messageSender = new MessageSender(ITEventsBot);

        GetEventsService getEventsService = new GetEventsService();
        TimerTask updateEvents = new TimerTask() {
            public void run() {
                getEventsService.getEventsList();
            }
        };
        Timer timer = new Timer("timer");
        long delay = 1000L;
        long period = 1000L * 60L * 60L * 24L;
        timer.scheduleAtFixedRate(updateEvents, delay, period);

        ITEventsBot.botConnect();

        Thread messageReceiverThread = new Thread(messageReceiver);
        messageReceiverThread.setDaemon(true);
        messageReceiverThread.setName("receiver");
        messageReceiverThread.setPriority(PRIORITY_FOR_RECEIVER);
        messageReceiverThread.start();

        Thread messageSenderThread = new Thread(messageSender);
        messageSenderThread.setDaemon(true);
        messageSenderThread.setName("sender");
        messageSenderThread.setPriority(PRIORITY_FOR_SENDER);
        messageSenderThread.start();

        sendStartReport(ITEventsBot);
    }

    private static void sendStartReport(Bot bot) {
        SendMessage sendMessage = SendMessage.builder().chatId(BOT_ADMIN_ID).text("Бот запущен").build();
        bot.sendQueue.add(sendMessage);
    }
}
