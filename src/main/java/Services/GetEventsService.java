package Services;

import Model.Event;
import Model.HibernateConnection;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public class GetEventsService {
    public static final int DEFAULT_EVENTS_QUANTITY = 20;
    private Session session = HibernateConnection.getSession();
    private List<Event> events = getEventsList();
    private StringBuffer eventInfo;
    private StringBuffer eventsToString;

    private List<Event> getEventsList() {
        ParseEventsService.parseEvents();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> rootEntry = criteriaQuery.from(Event.class);
        CriteriaQuery<Event> all = criteriaQuery.select(rootEntry);

        TypedQuery<Event> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }

    private String getFormattedEventInfo(Event event) {
        eventInfo = new StringBuffer();
        eventInfo.append("Тип: ").append(event.getType())
                .append("\t").append("Название события: ").append(event.getTitle())
                .append("\t").append("Дата проведения: ").append(event.getDate())
                .append("\t").append("Место проведения: ");
        if (!event.getOnline().isEmpty() && !event.getLocation().isEmpty()) {
            eventInfo.append(event.getOnline()).append(", ").append(event.getLocation());
        }
        if (!event.getOnline().isEmpty() && event.getLocation().isEmpty()) {
            eventInfo.append(event.getOnline());
        }
        else { eventInfo.append(event.getLocation()); }
        eventInfo.append("\t").append("Ссылка на событие: ").append(event.getLink());

        return eventInfo.toString();
    }

    public String getEvents() {
        eventsToString = new StringBuffer();
        for (int counter = 0; counter < DEFAULT_EVENTS_QUANTITY; counter++) {
            if (counter != 0) { eventsToString.append("\t\t"); }
            eventsToString.append(getFormattedEventInfo(events.get(counter)));
        }

        return eventsToString.toString();
    }
}
