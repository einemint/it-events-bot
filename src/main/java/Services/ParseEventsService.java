package Services;

import Model.Event;
import Model.HibernateConnection;
import Config.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ParseEventsService {
    private final String DEFAULT_SITE_ADDRESS = "https://it-events.com/?type=upcoming";
    private final int EVENTS_IN_PAGE_QUANTITY = 20;

    private Session session = HibernateConnection.getSession();
    private Transaction transaction = session.beginTransaction();

    private Document document;
    private Elements itemLinks;
    private Elements eventsLinks;
    private Elements eventsInfoLinks;
    private String siteAddress = "";
    private int eventsQuantity = 0;
    private int pagesQuantity = getPagesQuantity();

    private int getPagesQuantity() {
        try {
            document = Jsoup.connect(DEFAULT_SITE_ADDRESS).get();
            itemLinks = document.select("li[class='nav-tabs-item nav-tabs-item_active nav-tabs-item_main']");
            eventsLinks = itemLinks.select("span[class='nav-tabs-item__count']");
            eventsQuantity = Integer.parseInt(eventsLinks.text());

            if ((eventsQuantity % EVENTS_IN_PAGE_QUANTITY) != 0) {
                pagesQuantity = eventsQuantity / EVENTS_IN_PAGE_QUANTITY + 1;
            } else pagesQuantity = eventsQuantity / EVENTS_IN_PAGE_QUANTITY;
        }
        catch (Exception exception) { Logger.getInstance().fatal(exception.getStackTrace()); }

        return pagesQuantity;
    }

    private void parseEventsFromPage(int pageNumber) {
        try {
            siteAddress = "https://it-events.com/?page=" + pageNumber + "&type=upcoming";
            document = Jsoup.connect(siteAddress).get();
            eventsInfoLinks = document.select("div[class='event-list-item']");

            for (Element element : eventsInfoLinks) {
                Event event = new Event();
                event.setType(element.select("div[class='event-list-item__type']").text());
                event.setTitle(element.select("a[class='event-list-item__title']").text());
                event.setLink("https://it-events.com" + element.select("a[class='event-list-item__title']").attr("href"));
                event.setDate(element.select("div[class='event-list-item__info']").text());
                event.setLocation(element.select("div[class='event-list-item__info event-list-item__info_location']").text());
                event.setOnline(element.select("div[class='event-list-item__info event-list-item__info_online']").text());
                session.persist(event);
            }
        }
        catch (Exception exception) { Logger.getInstance().fatal(exception.getStackTrace()); }
    }

    public void parseEvents() {
        for (int counter = 1; counter <= pagesQuantity; counter++) {
            parseEventsFromPage(counter);
        }
        transaction.commit();
        session.close();
    }
}
