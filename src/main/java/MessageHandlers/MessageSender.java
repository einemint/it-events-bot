package MessageHandlers;

import Bot.Bot;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Log4j2
public class MessageSender implements Runnable {
    private final int SENDER_SLEEP_TIME = 1000;
    private Bot bot;

    public MessageSender(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (Object object = bot.sendQueue.poll(); object != null; object = bot.sendQueue.poll()) {
                    send(object);
                }

                try {
                    Thread.sleep(SENDER_SLEEP_TIME);
                } catch (Exception exception) {
                    log.warn(exception.getStackTrace());
                }
            }
        }
        catch (Exception sendException) { log.fatal(sendException.getStackTrace()); }
    }

    private void send(Object object) {
        try {
            if (object instanceof SendMessage) {
                BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                bot.execute(message);
            } else log.warn("Can't recognize response format");
        }
        catch (Exception exception) { log.warn(exception.getStackTrace()); }
    }
}
