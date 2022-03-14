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
    private int eventsQuantity = 20;
    private String keyWord = "";
    private Session session = HibernateConnection.getSession();
    private ParseEventsService parseEventsService = new ParseEventsService();
    private List<Event> events = getEventsList();
    private ArrayList<String> formattedEvents = new ArrayList<>();
    private StringBuffer eventInfo;

    private List<Event> getEventsList() {
        parseEventsService.parseEvents();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> rootEntry = criteriaQuery.from(Event.class);
        CriteriaQuery<Event> all = criteriaQuery.select(rootEntry);
        TypedQuery<Event> allQuery = session.createQuery(all);

        return allQuery.setFirstResult(0).setMaxResults(eventsQuantity).getResultList();
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
        else if (!event.getOnline().isEmpty() && event.getLocation().isEmpty()) {
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

        if (!keyWord.isEmpty()) {
            formattedEvents.removeIf(event -> !event.contains(keyWord));
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
