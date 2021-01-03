import Interfaces.Fetcher;
import Interfaces.JsonParser;
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

    private static String GOAL_BASE_URI = "https://api-metrika.yandex.net/management/v1/counter/";
    private static String COUNTERS_URI = "https://api-metrika.yandex.net/management/v1/counters";
    private static String JANDEX_STAT = "https://api-metrika.yandex.net/stat/v1/data?";
    private static String JANDEX_STAT_COMPARE = "https://api-metrika.yandex.net/stat/v1/data/comparison?";
    private static String JANDEX_STAT_BY_TIME = "https://api-metrika.yandex.net/stat/v1/data/bytime?";

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

        String responseBody = fetcher.fetch(COUNTERS_URI);
        Map<String, Object> jsonResponse = parser.parse(responseBody);
        List counters = (List) jsonResponse.get("counters");
        Transaction tx = session.beginTransaction();
        counters.stream()
                .map(counter -> WebpageManager.create((Map) counter))
                    .forEach(webpage -> WebpageManager.saveWebpageToDB(session, (Webpage) webpage));
        tx.commit();

    }

    public void fetchAllGoals(Webpage webpage, Goal goal) {
        LocalDate creationDate = convertToDate(webpage.getCreateTime()).plusDays(1);
        System.out.println(webpage.getName() + " - " + webpage.getPageId());
        System.out.println(goal.getName());
        StringBuilder sb = new StringBuilder();
        sb.append(JANDEX_STAT_BY_TIME).append("ids=")
                .append(webpage.getPageId())
                .append("&group=day&metrics=ym:s:goal")
                .append(goal.getGoalId()).append("reaches&dimensions=")
                .append("ym:s:lastsignTrafficSource")
                .append("&date1=").append(creationDate)
                .append("&date2=2020-12-31");
        System.out.println(sb.toString());
        String responseBody = fetcher.fetch(sb.toString());
        Map<String, Object> jsonResponse = parser.parse(responseBody);
        List timeIntervals = (List) jsonResponse.get("time_intervals");
        List data = (List) jsonResponse.get("data");
        Map<String, List> dataMap = (Map<String, List>) data.stream().collect(Collectors.toMap(
                entry -> (String) ((Map) ((List) ((Map) entry).get("dimensions")).get(0)).get("id"),
                entry -> (List) ((List) ((Map) entry).get("metrics")).get(0)
        ));
        Transaction tx = session.beginTransaction();
        for (int i = 0; i < timeIntervals.size(); i++) {
            GoalReachesTrafficSource newGoal = new GoalReachesTrafficSource();
            List dateRange = (List) timeIntervals.get(i);
            LocalDate date = LocalDate.parse((String) dateRange.get(0));

            newGoal.setGoal(goal);
            newGoal.setWebpage(webpage);
            newGoal.setDate(date);
            newGoal.setInternal(dataMap.get("internal") == null ? 0 :(Double) dataMap.get("internal").get(i));
            newGoal.setAd(dataMap.get("ad") == null ? 0 :(Double) dataMap.get("ad").get(i));
            newGoal.setReferral(dataMap.get("referral") == null ? 0 :(Double) dataMap.get("referral").get(i));
            newGoal.setSocial(dataMap.get("social") == null ? 0 :(Double) dataMap.get("social").get(i));
            newGoal.setDirect(dataMap.get("direct") == null ? 0 :(Double) dataMap.get("direct").get(i));
            newGoal.setRecommend(dataMap.get("recommend") == null ? 0 : (Double) dataMap.get("recommend").get(i));
            newGoal.setOrganic(dataMap.get("organic") == null ? 0 :(Double) dataMap.get("organic").get(i));
            session.persist(newGoal);
        }
        tx.commit();
    }

    public void fetchCounterGoals(Webpage webpage) {
        Transaction tx = session.beginTransaction();
        String responseBody = fetcher.fetch(GOAL_BASE_URI + webpage.getPageId() + "/goals");
        Map<String, Object> jsonResponse = parser.parse(responseBody);
        List goals = (List) jsonResponse.get("goals");
        goals.stream().map(goal -> GoalManager.createGoal(
                (Long) ((Map) goal).get("id"),
                (String) ((Map) goal).get("name"),
                (String) ((Map) goal).get("type"),
                webpage
        )).peek(goal -> System.out.println(((Goal) goal).getName() + ((Goal) goal).getGoalId()))
                .forEach(goal ->GoalManager.saveGoalToDB(session, (Goal) goal));
        tx.commit();
    }




}
