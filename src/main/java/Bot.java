import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import Config.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final int RECONNECT_PAUSE = 10000;
    @Setter
    @Getter
    private String botName;
    @Setter
    @Getter
    private String token;

    public Bot (String botName, String token) {
        this.botName = botName;
        this.token = token;
    }

    public Queue sendQueue = new ConcurrentLinkedQueue();
    public Queue receiveQueue = new ConcurrentLinkedQueue();

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
