import Implementation.SessionManagerImp;
import Interfaces.Fetcher;
import Interfaces.JsonParser;
import components.MetrikaUtils;
import managers.GoalManager;
import managers.TrafficSourceManager;
import managers.WebpageManager;
import models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.LongType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DatabaseUpdater {

    private static List updateList;
    private static LocalDate yesterday = LocalDate.now().minusDays(1);
    private static String JANDEX_STAT_BY_TIME = "https://api-metrika.yandex.net/stat/v1/data/bytime?ids=";

    private static String checkUpdateQuery =
            "WITH update AS (SELECT DISTINCT webpage_id AS wid FROM goalreachestrafficsource " +
            "WHERE date = '" + yesterday + "' GROUP BY webpage_id) SELECT DISTINCT " +
            "pageId FROM webpage WHERE pageId NOT IN (SELECT wid FROM update);";

    private static String goalsColumns =  TrafficSourceManager
            .trafficList.stream().collect(Collectors.joining(", "));

    private static String insertQuery =
            "INSERT INTO goalreachestrafficsource (id, webpage_id, goal_id, date, " + goalsColumns + ", total)\nVALUES\t";

    private Session session;
    private Fetcher fetcher;
    private JsonParser parser;

    public DatabaseUpdater(Fetcher fetcher, JsonParser parser, Session session) {
        this.session = session;
        this.fetcher = fetcher;
        this.parser = parser;
    }

    public void updateDatabase() {
        if (!DBIsUpToDate()) {
            List<Webpage> webpages = session.byMultipleIds(Webpage.class)
                    .enableSessionCheck(true).multiLoad(updateList);

            webpages.stream().peek(w -> System.out.println(w.getPageId() + " - " + w.getName()))
                    .map(webpage -> fetchGoalsFromMetrika(webpage))
                    .flatMap(responseList -> responseList.stream())
                    .map(response -> createInsertQuery(response))
                    .forEach(insertQuery -> executeInsertQuery(insertQuery));
        } else {
            System.out.println("Traffic source is upToDate");
        }
    }

    private boolean DBIsUpToDate() {

        Transaction tx = session.beginTransaction();
        List<Long> updateList = session.createSQLQuery(checkUpdateQuery)
                .addScalar("pageId", new LongType()).list();
        this.updateList = updateList.stream()
                .filter(id -> !WebpageManager.wpWithoutGoalsMap.containsKey(id))
                .collect(Collectors.toList());
        tx.commit();

        return this.updateList.size() == 0;
    }

    private List<Map<String, Object>> fetchGoalsFromMetrika(Webpage webpage) {
        List<Goal> goals = GoalManager.getAllGoalsFromDBForCounter(new SessionManagerImp(), webpage);
        List<Map<String, Object>> responseList = goals.size() > 15
                ? fetchMultGoalsData(goals, webpage.getPageId())
                : Arrays.asList(fetchGoalsData(goals, webpage.getPageId()));
        return responseList;
    }

    private List<Map<String, Object>> fetchMultGoalsData(List<Goal> goals, Long webpageId) {
        return MetrikaUtils.splitGoals(goals).stream()
                .map(goal -> fetchGoalsData(goal, webpageId)).collect(Collectors.toList());
    }

    private Map<String, Object> fetchGoalsData(List<Goal> goals, Long webpageId) {
        String request = createRequest(goals, webpageId);
        Map<String, Object> response = parser.parse(fetcher.fetch(request));
        response.put("webpage_id", webpageId);
        return response;
    }

    private static String createRequest(List<Goal> goals, Long webpageId) {
        String goalsMetrik = goals.stream()
                .map(goal -> "ym:s:goal" + goal.getGoalId() + "reaches")
                .collect(Collectors.joining(","));

        StringBuilder request = new StringBuilder();
        request.append(JANDEX_STAT_BY_TIME).append(webpageId)
                .append("&group=day&metrics=").append(goalsMetrik)
                .append("&dimensions=ym:s:lastsignTrafficSource")
                .append("&date1=").append(yesterday)
                .append("&date2=").append(yesterday);

        return request.toString();
    }

    private String createInsertQuery(Map<String, Object> response) {

        String insertQuery;

        List<Map<String, List>> data = (List) response.get("data");
        Map<String, Object> query = (Map) response.get("query");
        List<Long> goals = parseGoalsIds((List) query.get("metrics"));
        Long webpageId = (Long) response.get("webpage_id");

        if (data.size() == 0) {
            insertQuery = createEmptyQuery(webpageId, goals);
        } else {
            Map<Long, String> metrikaValues = TrafficSourceManager.mapGoalsToTrafficSource(goals, data);
            insertQuery = createNonEmptyQuery(metrikaValues, webpageId);
        }

        return insertQuery;
    }

    private static List<Long> parseGoalsIds(List<String> goalsIds) {
        Pattern goalPattern = Pattern.compile("ym:s:goal(\\d+)reaches");
        List<Long> parsedGoals = goalsIds.stream().map(goal -> goalPattern.matcher(goal))
                .map(matcher -> {
                    matcher.find();
                    return  Long.valueOf(matcher.group(1));
                }).collect(Collectors.toList());
        return parsedGoals;
    }


    private static String createNonEmptyQuery(Map<Long, String> metrikaValues, Long webpageId) {
        String insertValues = metrikaValues.entrySet().stream()
                .map(goalData -> nonEmptyQueryForGoal(webpageId, goalData))
                .collect(Collectors.joining(",\n  \t\t"));
        return insertQuery + insertValues + ";";
    }

    private static String nonEmptyQueryForGoal(Long webpageId, Map.Entry goalsData) {
        return "(nextval('hibernate_sequence'), " + webpageId + ", "
                + goalsData.getKey() + ", '"+  yesterday + "', " + goalsData.getValue() + ")";
    }

    private static String createEmptyQuery(Long webpageId, List<Long> goalIds) {
        String insertValues = goalIds.stream()
                .map(goalId -> emptyQueryForGoal(webpageId, goalId))
                .collect(Collectors.joining(",\n  \t\t"));
        return insertQuery + insertValues + ";";
    }

    private static String emptyQueryForGoal(Long webpageId, Long goalId) {
        return "(nextval('hibernate_sequence'), " + webpageId + ", "+ goalId + ", '"+  yesterday + "', 0, 0, 0, 0, 0, 0, 0, 0)";
    }

    private void executeInsertQuery(String insertQuery) {
        Transaction tx = session.beginTransaction();
        NativeQuery query = session.createSQLQuery(insertQuery);
        query.executeUpdate();
        tx.commit();
    }

}
