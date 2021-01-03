package Sources.SearchEngine;

import Sources.Abstract.DatabaseUpdaterAbs;
import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Interfaces.SessionManager;
import models.GoalsReachesBySearchEngine;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SearchEngineUpdater extends DatabaseUpdaterAbs {

    private SearchEngineManager sourceManager;
    private static String dimensions = "ym:s:lastsignSearchEngineRoot";

    private static String insertQuery =
            "INSERT INTO goalssearchengine (id, webpage_id, date, goal_id, engine_id, reaches)\nVALUES\n\t";

    public SearchEngineUpdater(SessionManager manager, Fetcher fetcher, JsonParser parser) {
        super(GoalsReachesBySearchEngine.class, dimensions, manager, fetcher, parser);
        this.sourceManager = new SearchEngineManager(manager);
    }


    @Override
    protected String createStatement(List<Map<String, List>> data, List<Long> goals, Long webpageId) {

        String insertQuery = null;
        if (data.size() != 0) {
            List<String> insertValues = sourceManager.mapGoalsToSource(data, goals);
            insertQuery = createNonEmptyInsertQuery(insertValues, webpageId);
        } else {
            insertQuery =  createEmptyInsertQuery(goals, webpageId);
        }
        return insertQuery;
    }

    private String createNonEmptyInsertQuery(List<String> insertValues, Long webpageId) {
        String values = insertValues.stream()
                .map(value ->
                        "(nextval('hibernate_sequence'), " + webpageId + ", '" + yesterday + "', " + value + ")"
                ).collect(Collectors.joining(",\n  \t\t"));

        return insertQuery + values + ";";
    }

    private String createEmptyInsertQuery(List<Long> goals, Long webpageId) {
        String values =  "(nextval('hibernate_sequence'), " + webpageId + ", '" + yesterday + "', " + goals.get(0) + ", 'yandex', 0)";
        return insertQuery + values + ";";
    }
}
