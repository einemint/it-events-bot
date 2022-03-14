import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class App {
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 2;
    private static final String BOT_ADMIN_ID = "5147810668";

    public static void main(String[] args) {
        Bot ITEventsBot = new Bot("it_events_fresher_bot", "5106867496:AAEu6VF0UA-eIADcw8OOL5YeDbIhY-ui7N0");

        MessageReceiver messageReceiver = new MessageReceiver(ITEventsBot);
        MessageSender messageSender = new MessageSender(ITEventsBot);

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
