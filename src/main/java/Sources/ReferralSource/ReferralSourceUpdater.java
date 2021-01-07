package Sources.ReferralSource;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.Abstract.DatabaseUpdaterAbs;
import Sources.Abstract.SourceManager;
import models.goals.GoalsReachesByReferralSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReferralSourceUpdater extends DatabaseUpdaterAbs {

    private SourceManager sourceManager;
    private static String dimensions = "ym:s:lastsignReferalSource";

    private static String insertQuery =
            "INSERT INTO goalsreferral (id, webpage_id, date, goal_id, referral_id, reaches)\nVALUES\n\t";

    public ReferralSourceUpdater(SourceManager sourceManager, Fetcher fetcher, JsonParser parser) {
        super(GoalsReachesByReferralSource.class, dimensions, fetcher, parser);
        this.sourceManager = sourceManager;
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
        String values =  "(nextval('hibernate_sequence'), " + webpageId + ", '" + yesterday + "', " + goals.get(0) + ", '-', 0)";
        return insertQuery + values + ";";
    }

}
