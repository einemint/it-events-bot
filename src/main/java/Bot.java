import Log.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bot extends TelegramLongPollingBot {
    public Bot (String botName, String token) {
        this.botName = botName;
        this.token = token;
    }

    public Bot() {}

    private final int RECONNECT_PAUSE = 10000;
    private String botName;
    private String token;

    public Queue sendQueue = new ConcurrentLinkedQueue();
    public Queue receiveQueue = new ConcurrentLinkedQueue();

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        receiveQueue.add(update);
    }

    public void botConnect() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        }
        catch (Exception exception) {
            Logger.getInstance().fatal(exception.getStackTrace());

            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (Exception reconnectionException) {
                Logger.getInstance().fatal(reconnectionException.getStackTrace());
                return;
            }
            botConnect();
        }
    }
}
