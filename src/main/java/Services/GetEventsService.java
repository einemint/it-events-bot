package Services;

import Model.Event;
import Model.HibernateConnection;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;

import java.util.*;
import java.util.stream.Collectors;

public class GetEventsService {
    @Getter
    private int eventsQuantity = 20;
    @Setter
    @Getter
    private String keyWord = "";
    private Session session = HibernateConnection.getSession();
    private ParseEventsService parseEventsService = new ParseEventsService();
    private List<Event> events = getEventsList();
    private ArrayList<String> formattedEvents = new ArrayList<>();
    private StringBuffer eventInfo;

    private void updateEventsInDB() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                getEventsList();
            }
        };
        Timer timer = new Timer("timer");

        long delay = 1000L;
        long period = 1000L * 60L * 60L * 24L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    private List<Event> getEventsList() {
        parseEventsService.parseEvents();
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
        updateEventsInDB();
        formattedEvents.clear();
        if (keyWord.isEmpty()) {
            for (int counter = 0; counter < eventsQuantity; counter++) {
                formattedEvents.add(getFormattedEventInfo(events.get(counter)));
            }
        }

        else {
            for (Event eventInstance : events) {
                formattedEvents.add(getFormattedEventInfo(eventInstance));
                formattedEvents.removeIf(event -> !event.contains(keyWord));
            }

            if (formattedEvents.size() > eventsQuantity) {
                formattedEvents = (ArrayList<String>) formattedEvents.stream().limit(eventsQuantity).collect(Collectors.toList());
            }
        }

        return formattedEvents;
    }

    public void setEventsQuantity(int quantity) {
        if (quantity <= 0) { eventsQuantity = 1; }
        else if (quantity >= events.size()) { eventsQuantity = events.size(); }
        else eventsQuantity = quantity;
    }
}
