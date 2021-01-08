package Sources.ReferralSource;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.Abstract.DatabaseGoalsFiller;
import Sources.Abstract.SourceManager;
import components.MetrikaUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReferralSourceGoalsFiller extends DatabaseGoalsFiller {

    private SourceManager sourceManager;
    private static String dimensions = "ym:s:lastsignReferalSource";

    private static String insertQuery =
            "INSERT INTO goalsreferral (id, webpage_id, goal_id, date, referral_id, reaches)\nVALUES\n\t";

    public ReferralSourceGoalsFiller(SourceManager sourceManager, LocalDate endDate,
                                     Fetcher fetcher, JsonParser parser)
    {
        super(dimensions, endDate, parser, fetcher);
        this.sourceManager = sourceManager;
    }

    @Override
    protected String createInsertQuery(Map<String, Object> response) {

        List timeStamps = ((List<List>) response.get("time_intervals")).stream()
                .map(interval -> interval.get(0)).collect(Collectors.toList());
        List<Map<String, List>> data = (List) response.get("data");

        if (data.size() > 0) {
            List<String> insertValues = parseInsertValues(data, timeStamps);
            String goalInsertValues = insertValues.stream().map(
                    insertValue -> "(nextval('hibernate_sequence'), " + response.get("webpageId") +
                            ", " + response.get("goalId") + ", " + insertValue + ")"
            ).collect(Collectors.joining(",\n\t"));
            return insertQuery + goalInsertValues + ";";
        }
        return null;
    }

    private List<String> parseInsertValues(List<Map<String, List>> data, List timeStamps) {
        Map<String, List> valuesMap = parseData(data);

        List<String> queries = new ArrayList<>();
        for (String key: valuesMap.keySet()) {
            for (int i = 0; i < timeStamps.size(); i++) {
                queries.add("'" + timeStamps.get(i) + "', '" + key + "', " + valuesMap.get(key).get(i));
            }
        }
        return queries;
    }

    private Map<String, List> parseData(List<Map<String, List>> data) {
        return data.stream().peek(source -> sourceManager.createNewSourceInstance(source))
                .collect(Collectors.toMap(MetrikaUtils::getDimensionName, entry -> (List) entry.get("metrics").get(0)));
    }

}
