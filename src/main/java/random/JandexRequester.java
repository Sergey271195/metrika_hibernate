package random;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import components.MetrikaUtils;
import managers.GoalManager;
import managers.WebpageManager;
import models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JandexRequester {

    private Fetcher fetcher;
    private JsonParser parser;
    private Session session;

    public static LocalDate convertToDate(ZonedDateTime dateTime) {
        String year = String.valueOf(dateTime.getYear());
        String month = dateTime.getMonthValue() >= 10 ?  String.valueOf(dateTime.getMonthValue()) : "0"+dateTime.getMonthValue();
        String day = dateTime.getDayOfMonth() >= 10 ?  String.valueOf(dateTime.getDayOfMonth()) : "0"+dateTime.getDayOfMonth();
        LocalDate date = LocalDate.parse(year+"-"+month+"-"+day);
        return date;
    }

    public JandexRequester(Fetcher fetcher, JsonParser parser, Session session) {
        this.fetcher = fetcher;
        this.parser = parser;
        this.session = session;
    }

    public void refreshRegisteredWebpages() {

        String responseBody = fetcher.fetch(MetrikaUtils.COUNTERS_URI);
        Map<String, Object> jsonResponse = parser.parse(responseBody);
        List counters = (List) jsonResponse.get("counters");
        Transaction tx = session.beginTransaction();
        counters.stream()
                .map(counter -> WebpageManager.create((Map) counter))
                    .forEach(webpage -> WebpageManager.saveWebpageToDB(session, (Webpage) webpage));
        tx.commit();

    }

}
