package models;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Interfaces.SessionManager;
import components.MetrikaUtils;
import managers.WebpageManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WebpageUpdater {

    private static final List<Long> relevantCounters = Arrays.asList(
            39578050L, 26059395L, 24332956L, 29736370L, 48050831L, 24596720L, 23258257L, 15070123L, 19915630L,
            56140561L, 45487302L, 55811590L, 20548771L, 54241003L, 62401888L, 49911565L, 62111218L, 54131236L,
            57391372L, 44494876L, 62095546L, 52392583L, 59162569L
    );

    public static void updateRelevantList(SessionManager sessionManager) {
        Session session = sessionManager.startSession();
        List<Long> webpages = WebpageManager.getAllWebpageIdsList(session);
        Transaction tx = session.beginTransaction();
        for (Long id: webpages) {
            if (!relevantCounters.contains(id)) {
                Webpage unrelevantCounter = session.get(Webpage.class, id);
                unrelevantCounter.setActual(false);
                System.out.println("Setting relevancy to false for counter: " + unrelevantCounter.getName()
                + " - " + unrelevantCounter.getPageId());
            }
        }
        tx.commit();
        session.close();
    }

    public static void SetCommercialWebpages(Fetcher fetcher, JsonParser parser, SessionManager sessionManager) {
        Session session = sessionManager.startSession();
        List<Long> webpages = WebpageManager.getAllWebpageIdsList(session);

        Transaction tx = session.beginTransaction();
        for (Long webpageId: webpages) {
            String checkQuery = MetrikaUtils.JANDEX_STAT_BY_TIME + webpageId +
                    "&metrics=ym:s:ecommercePurchases&date1=2020-01-03&date2=2020-01-03";
            Map<String, Object> response = parser.parse(fetcher.fetch(checkQuery));
            if (response.get("error") == null) {
                Webpage wp = session.get(Webpage.class, webpageId);
                wp.setCommercial(true);
            }
        }
        tx.commit();
        session.close();

    }

}
