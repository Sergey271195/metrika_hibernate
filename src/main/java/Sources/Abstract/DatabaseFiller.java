package Sources.Abstract;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import components.MetrikaUtils;
import components.SessionWrapper;
import components.UpdateQueryExecutor;
import managers.GoalManager;
import managers.WebpageManager;
import models.Goal;
import models.GoalReachesTrafficSource;
import models.Webpage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DatabaseFiller {

    private final String dimensions;
    private final LocalDate endDate;
    private final Fetcher fetcher;
    private final JsonParser parser;

    public DatabaseFiller(String dimensions, LocalDate endDate, JsonParser parser, Fetcher fetcher) {
        this.dimensions = dimensions;
        this.endDate = endDate;
        this.parser = parser;
        this.fetcher = fetcher;
    }

    public void fillDatabase() {
        SessionWrapper.wrap(this::wrap);
    }

    public Object wrap(Session session) {
        this.__fillDatabase__(session);
        return this;
    }

    private void __fillDatabase__(Session session) {
        List<Webpage> webpages = WebpageManager.fetchWebpagesFromDB(session);
        for (Webpage webpage: webpages.subList(0, 1)) {
            List<Goal> goals = GoalManager.getAllGoalsFromDBForCounter(session, webpage);
            goals.stream().map(goal -> fetchGoalReaches(webpage, goal))
                    .map(this::createInsertQuery).forEach(query -> System.out.println(query));
                    //.forEach(this::executeInsertQuery);
        }
    }

    private Map<String, Object> fetchGoalReaches(Webpage webpage, Goal goal) {
        String request = createRequest(webpage, goal);
        Map<String, Object> response = parser.parse(fetcher.fetch(request));
        return response;
    }

    private String createRequest(Webpage webpage, Goal goal) {
        LocalDate creationDate = MetrikaUtils.convertToDate(webpage.getCreateTime()).plusDays(1);
        StringBuilder request = new StringBuilder();
        request.append(MetrikaUtils.JANDEX_STAT_BY_TIME).append(webpage.getPageId())
                .append("&group=day&metrics=ym:s:goal").append(goal.getGoalId())
                .append("reaches&dimensions=").append(dimensions)
                .append("&date1=").append(creationDate)
                .append("&date2=").append(endDate);

        return request.toString();
    }

    private String createInsertQuery(Map<String, Object> response) {
        List timeStamps = ((List<List>) response.get("time_intervals")).stream()
                .map(interval -> interval.get(0)).collect(Collectors.toList());
        List<Map<String, List>> data = (List) response.get("data");
        return createStatement(data, timeStamps);
    }

    private void executeInsertQuery(String insertQuery) {
        SessionWrapper.wrapWithStringArg(new UpdateQueryExecutor(), insertQuery);
    }

    protected abstract String createStatement(List<Map<String, List>> data, List timeStamp);

}
