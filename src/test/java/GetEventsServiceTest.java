import Model.Event;
import Service.GetEventsService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class GetEventsServiceTest extends TestCase {
    private GetEventsService getEventsService;
    private Event event;

    private String type = "Конференция";
    private String title = "AV FOCUS Казань";
    private String online = "";
    private String location = "Казань, Россия";
    private String link = "https://it-events.com/events/22772";
    private String date = "24 марта 2022";

    private String correctEventInfo = "Тип: Конференция\n" +
            "Название события: AV FOCUS Казань\n" +
            "Дата проведения: 24 марта 2022\n" +
            "Место проведения: Казань, Россия\n" +
            "Ссылка на событие: https://it-events.com/events/22772";

    public void setUp() throws Exception {
        getEventsService = new GetEventsService();
        event = new Event();
        event.setType(type);
        event.setTitle(title);
        event.setOnline(online);
        event.setLocation(location);
        event.setLink(link);
        event.setDate(date);
    }

    @Test
    @DisplayName("Некорректный вывод данных событий")
    public void testGetFormattedEventInfo() {
        String correctEventInfoResult = getEventsService.getFormattedEventInfo(event);

        assertEquals(correctEventInfoResult, correctEventInfo);
    }
}
