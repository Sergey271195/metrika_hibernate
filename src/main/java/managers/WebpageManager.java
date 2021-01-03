package managers;

import models.Webpage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebpageManager {

    public static List<Long> webpagesWithoutGoals =
            Arrays.asList(
                    65064478L, 67865518L, 65156065L, 56247157L, 53667862L,
                    37960820L, 42267299L, 58976794L, 67426333L, 69704290L
            );
    public static Map<Long, Long> wpWithoutGoalsMap =
            webpagesWithoutGoals.stream().collect(Collectors.toMap(id -> id, id -> id));

    public static Webpage create(Map data) {

        Webpage wp = new Webpage();
        wp.setPageId((Long) data.get("id"));
        wp.setName((String) data.get("name"));
        wp.setSite((String) data.get("site"));
        wp.setCreateTime(ZonedDateTime.parse((String) data.get("create_time")));
        return wp;

    }

    public static List<Webpage> fetchWebpagesFromDB (Session session) {
        Transaction tx = session.beginTransaction();
        List<Webpage> webpages = session
                .createQuery("SELECT m FROM Webpage m", Webpage.class)
                    .getResultList();
        tx.commit();
        return webpages;
    }

    public static void saveWebpageToDB (Session session, Webpage webpage) {
        session.persist(webpage);
    }

    public static List<Long> getAllWebpageIdsList (Session session) {
        Transaction tx = session.beginTransaction();
        List pageIds = session.createSQLQuery("SELECT pageId FROM webpage")
                .addScalar("pageId", new LongType()).list();
        tx.commit();
        return pageIds;
    }

    public static List<Long> getWebpageWithGoalsIdsList (Session session) {

        String pagesWithoutGoals = webpagesWithoutGoals.stream()
                .map(goalId -> goalId.toString())
                .collect(Collectors.joining(","));
        Transaction tx = session.beginTransaction();
        List pageIds = session.createSQLQuery(
                "SELECT pageId FROM webpage WHERE pageID NOT IN (" + pagesWithoutGoals + ");"
        ).addScalar("pageId", new LongType()).list();
        tx.commit();
        return pageIds;
        
    }

}