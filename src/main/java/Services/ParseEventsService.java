package Services;

import Log.Logger;
import Model.Event;
import Model.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseEventsService {
    public static final String DEFAULT_SITE_ADDRESS = "https://it-events.com/?type=upcoming";
    public static final int EVENTS_IN_PAGE_QUANTITY = 20;

    private static Session session = HibernateConnection.getSession();
    private static Transaction transaction = session.beginTransaction();

    private static Document document;
    private static Elements itemLinks;
    private static Elements eventsLinks;
    private static Elements eventsInfoLinks;
    private static String siteAddress = "";
    private static int eventsQuantity = 0;
    private static int pagesQuantity = getPagesQuantity();

    private static int getPagesQuantity() {
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

    private static synchronized void parseEventsFromPage(int pageNumber) {
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

    public static synchronized void parseEvents() {
        for (int counter = 1; counter <= pagesQuantity; counter++) {
            parseEventsFromPage(counter);
        }
        transaction.commit();
        session.close();
    }
}
