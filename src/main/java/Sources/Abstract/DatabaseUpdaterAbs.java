package Sources.Abstract;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Interfaces.SessionManager;
import components.MetrikaUtils;
import components.SessionWrapper;
import components.UpdateChecker;
import components.UpdateQueryExecutor;
import managers.GoalManager;
import models.Goal;
import models.Webpage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class DatabaseUpdaterAbs {

    private final Class tableClass;
    private final Fetcher fetcher;
    private final JsonParser parser;
    private final String dimensions;
    private UpdateChecker checker;
    private static String JANDEX_STAT_BY_TIME = "https://api-metrika.yandex.net/stat/v1/data/bytime?ids=";

    protected static LocalDate yesterday = UpdateChecker.yesterday;

    public DatabaseUpdaterAbs(
            Class tableClass, String dimensions, Fetcher fetcher, JsonParser parser
    ) {
        this.tableClass = tableClass;
        this.dimensions = dimensions;
        this.fetcher = fetcher;
        this.parser = parser;
        this.checker = new UpdateChecker(tableClass);
    }

    public void updateDatabase() {
        if (!dbIsUpToDate()) {
            List<Webpage> webpagesToUpdate = checker.getUpdateList();

            webpagesToUpdate.stream()
                    .peek(w -> System.out.println(this.getClass().getSimpleName() + " - " + w.getPageId() + " - " + w.getName()))
                    .map(webpage -> fetchGoalsFromMetrika(webpage))
                    .flatMap(responseList -> responseList.stream())
                    .map(response -> createInsertQuery(response)).filter(Objects::nonNull)
                    .forEach(insertQuery -> executeInsertQuery(insertQuery));
        } else {
            System.out.println(tableClass.getSimpleName() + " is up-to-date");
        }
    }

    private List<Map<String, Object>> fetchGoalsFromMetrika(Webpage webpage) {
        List<Goal> goals = new GoalManager().getGoalsForCounterWrapped(webpage);
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

    private String createRequest(List<Goal> goals, Long webpageId) {
        String goalsMetrik = goals.stream()
                .map(goal -> "ym:s:goal" + goal.getGoalId() + "reaches")
                .collect(Collectors.joining(","));

        StringBuilder request = new StringBuilder();
        request.append(JANDEX_STAT_BY_TIME).append(webpageId)
                .append("&group=day&metrics=").append(goalsMetrik)
                .append("&dimensions=").append(dimensions)
                .append("&date1=").append(yesterday)
                .append("&date2=").append(yesterday);

        return request.toString();
    }

    protected static List<Long> parseGoalsIds(List<String> goalsIds) {
        Pattern goalPattern = Pattern.compile("ym:s:goal(\\d+)reaches");
        List<Long> parsedGoals = goalsIds.stream().map(goal -> goalPattern.matcher(goal))
                .map(matcher -> {
                    matcher.find();
                    return  Long.valueOf(matcher.group(1));
                }).collect(Collectors.toList());
        return parsedGoals;
    }

    private String createInsertQuery(Map<String, Object> response) {

        List<Map<String, List>> data = (List) response.get("data");
        Map<String, Object> query = (Map) response.get("query");
        List<Long> goals = parseGoalsIds((List) query.get("metrics"));
        Long webpageId = (Long) response.get("webpage_id");

        return createStatement(data, goals, webpageId);
    }

    private void executeInsertQuery(String insertQuery) {
        SessionWrapper.wrapWithStringArg(new UpdateQueryExecutor(), insertQuery);
    }

    private boolean dbIsUpToDate() { return SessionWrapper.wrap(checker); };

    protected abstract String createStatement(List<Map<String, List>> data, List<Long> goals, Long webpageId);

}
