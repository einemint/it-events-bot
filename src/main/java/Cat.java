import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public class Cat {
    private final String FILE_PATH = "src/main/java/Data/Images/352cf78108fc75e.webp";
    private InputFile cat = new InputFile(FILE_PATH);

    public void executeCommand(Long chatId, Bot bot) {
        SendPhoto sendPhoto = SendPhoto.builder().chatId(Long.toString(chatId)).photo(cat).build();
        bot.sendQueue.add(sendPhoto);
    }
}
