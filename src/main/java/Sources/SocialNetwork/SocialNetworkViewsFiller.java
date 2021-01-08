package Sources.SocialNetwork;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.Abstract.DatabaseViewsFiller;
import Sources.Abstract.SourceManager;
import Sources.AdvEngine.AdvEngineConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SocialNetworkViewsFiller extends DatabaseViewsFiller {

    private SourceManager sourceManager;
    private static String dimensions = "ym:s:lastsignSocialNetwork";
    private SocialNetworkConstants CONSTANTS = new SocialNetworkConstants();

    private static Map<String, String> queriesMap = new HashMap<>();

    public SocialNetworkViewsFiller(SourceManager sourceManager, LocalDate endDate, Fetcher fetcher, JsonParser parser) {
        super(dimensions, endDate, parser, fetcher);
        this.sourceManager = sourceManager;
    }

    @Override
    protected String createInsertQuery(Map<String, Object> response) {

        List<String> insertQueries = new ArrayList();

        List timeStamps = ((List<List>) response.get("time_intervals")).stream()
                .map(interval -> interval.get(0)).collect(Collectors.toList());
        List<Map<String, List>> data = (List) response.get("data");
        Map<String, Object> query = (Map<String, Object>) response.get("query");
        List<String> metrics = (List) query.get("metrics");
        Long webpageId = (Long) ((List) query.get("ids")).get(0);

        if (data.size() > 0) {
            Map<String, Map<String, List>> insertValues = sourceManager.mapMetricsToSourceHistory(data, metrics);
            for (String metrica: insertValues.keySet()) {
                List<String> parsedValues = parseInsertValues(insertValues.get(metrica), timeStamps);
                String parsedInsertValues = parsedValues.stream()
                        .map(value -> "(nextval('hibernate_sequence'), " + webpageId + ", " + value + ")")
                        .collect(Collectors.joining(",\n\t"));
                String insertQuery = CONSTANTS.getQueryMap().get(metrica);
                insertQueries.add(insertQuery + parsedInsertValues + ";");
            }
            return insertQueries.stream().collect(Collectors.joining("\n"));
        }
        return null;
    }

    private List<String> parseInsertValues(Map<String, List> data, List timeStamps) {

        List<String> queries = new ArrayList<>();
        for (String key: data.keySet()) {
            for (int i = 0; i < timeStamps.size(); i++) {
                queries.add("'" + timeStamps.get(i) + "', '" + key + "', " + data.get(key).get(i));
            }
        }
        return queries;
    }
}
