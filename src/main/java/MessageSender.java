import Log.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.awt.*;

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
                    Logger.getInstance().fatal(exception.getStackTrace());
                }
            }
        }
        catch (Exception sendException) { Logger.getInstance().fatal(sendException.getStackTrace()); }
    }

    private void send(Object object) {
        try {
            if (object instanceof SendMessage) {
                BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                bot.execute(message);
            }
            if (object instanceof SendPhoto) {
                BotApiMethod<PhotoSize> photo = (BotApiMethod<PhotoSize>) object;
                bot.execute(photo);
            }
            else Logger.getInstance().fatal("Can't recognize response format");
        }
        catch (Exception exception) { Logger.getInstance().fatal(exception.getStackTrace()); }
    }
}
