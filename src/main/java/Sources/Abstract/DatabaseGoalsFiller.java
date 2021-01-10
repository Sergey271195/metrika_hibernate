package Sources.Abstract;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import components.MetrikaUtils;
import components.SessionWrapper;
import components.UpdateQueryExecutor;
import managers.GoalManager;
import managers.WebpageManager;
import models.Goal;
import models.Webpage;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class DatabaseGoalsFiller {

    private final String dimensions;
    private final LocalDate endDate;
    private final Fetcher fetcher;
    private final JsonParser parser;

    public DatabaseGoalsFiller(String dimensions, LocalDate endDate, JsonParser parser, Fetcher fetcher) {
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
        List<Webpage> webpages = WebpageManager.fetchWebpagesFromDBWithoutTransaction(session);
        int counter = 0;
        for (Webpage webpage: webpages) {
            //Webpage webpage = session.get(Webpage.class, 62401888L);
            counter++;
            System.out.println(counter);
            List<Goal> goals = GoalManager.getAllGoalsFromDBForCounter(session, webpage);
            goals.stream()
                    .peek(goal -> System.out.println(this.getClass().getSimpleName() + " - " + webpage.getPageId() + " - " + goal.getName()))
                    .map(goal -> fetchGoalReaches(webpage, goal))
                    .map(this::createInsertQuery).filter(Objects::nonNull)
                    //.forEach(query -> System.out.println(query));
                    .forEach(this::executeInsertQuery);
        }
    }

    private Map<String, Object> fetchGoalReaches(Webpage webpage, Goal goal) {
        String request = createRequest(webpage, goal);
        Map<String, Object> response = parser.parse(fetcher.fetch(request));
        response.put("webpageId", webpage.getPageId());
        response.put("goalId", goal.getGoalId());
        System.out.println(response);
        return response;
    }

    private String createRequest(Webpage webpage, Goal goal) {
        LocalDate creationDate = MetrikaUtils.convertToDate(webpage.getCreateTime()).plusDays(1);
        StringBuilder request = new StringBuilder();
        request.append(MetrikaUtils.JANDEX_STAT_BY_TIME).append(webpage.getPageId())
                .append("&group=day&metrics=ym:s:goal").append(goal.getGoalId())
                .append("reaches&dimensions=").append(dimensions)
                .append("&date1=").append(creationDate)
                .append("&date2=").append(endDate).append("&limit=100000");

        return request.toString();
    }

    protected abstract String createInsertQuery(Map<String, Object> response);

    private void executeInsertQuery(String insertQuery) {
        SessionWrapper.wrapWithStringArg(new UpdateQueryExecutor(), insertQuery);
    }

}
