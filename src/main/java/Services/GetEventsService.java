package Services;

import Model.Event;
import Model.HibernateConnection;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetEventsService {
    private static int eventsQuantity = 20;
    private static String keyWord = "";
    private Session session = HibernateConnection.getSession();
    private List<Event> events = getEventsList();
    private ArrayList<String> formattedEvents = new ArrayList<>();
    private StringBuffer eventInfo;
    private StringBuffer eventsToString;

    private List<Event> getEventsList() {
        ParseEventsService.parseEvents();

        if (keyWord.isEmpty()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
            Root<Event> rootEntry = criteriaQuery.from(Event.class);
            CriteriaQuery<Event> all = criteriaQuery.select(rootEntry);

            TypedQuery<Event> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        }

        else {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
            Root<Event> rootEntry = criteriaQuery.from(Event.class);
            CriteriaQuery<Event> all = criteriaQuery.select(rootEntry);

            TypedQuery<Event> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        }
    }

    private String getFormattedEventInfo(Event event) {
        eventInfo = new StringBuffer();
        eventInfo.append("Тип: ").append(event.getType())
                .append("\n").append("Название события: ").append(event.getTitle())
                .append("\n").append("Дата проведения: ").append(event.getDate())
                .append("\n").append("Место проведения: ");
        if (!event.getOnline().isEmpty() && !event.getLocation().isEmpty()) {
            eventInfo.append(event.getOnline()).append(", ").append(event.getLocation());
        }
        if (!event.getOnline().isEmpty() && event.getLocation().isEmpty()) {
            eventInfo.append(event.getOnline());
        }
        else { eventInfo.append(event.getLocation()); }
        eventInfo.append("\n").append("Ссылка на событие: ").append(event.getLink());

        return eventInfo.toString();
    }

    public ArrayList<String> getEvents() {
        formattedEvents.clear();
        for (int counter = 0; counter < eventsQuantity; counter++) {
            formattedEvents.add(getFormattedEventInfo(events.get(counter)));
        }

        return formattedEvents;
    }

    public void setEventsQuantity(int quantity) {
        eventsQuantity = quantity;
    }

    public void setKeyWord(String text) {
        keyWord = text;
    }
}
