package Sources.ReferralSource;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.Abstract.DatabaseViewsFiller;
import Sources.Abstract.SourceManager;
import components.MetrikaUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReferralSourceViewsFiller extends DatabaseViewsFiller {

    private SourceManager sourceManager;
    private static String dimensions = "ym:s:lastsignReferalSource,ym:s:datePeriodday";
    private ReferralSourceConstants CONSTANTS = new ReferralSourceConstants();
    private final Fetcher fetcher;
    private final JsonParser parser;

    private List<String> requestMetrics;
    private Long webpageId;
    private String queryStatrtDate;
    private String queryEndDate;
    private List<String> requestDimensions;


    public ReferralSourceViewsFiller(SourceManager sourceManager, LocalDate endDate, Fetcher fetcher, JsonParser parser) {
        super(dimensions, endDate, parser, fetcher);
        this.sourceManager = sourceManager;
        this.fetcher = fetcher;
        this.parser = parser;
    }

    @Override
    protected String createInsertQuery(Map<String, Object> response) {
        List<Map<String, List>> data = (List) response.get("data");
        data.stream().forEach(entry -> ((ReferralSourceManager) sourceManager).createNewSourceInstanceForFiller(entry));
        String subQuery = createSubQuery(response);
        Map<String, List<String>> insertQueriesMap = handleResponse(data, subQuery);
        String finalQuery = createFinalQuery(insertQueriesMap);
        return finalQuery;
    }

    private void parseResponse(Map<String, Object> response) {
        Map<String, Object> query = (Map<String, Object>) response.get("query");
        webpageId = (Long) ((List) query.get("ids")).get(0);
        queryStatrtDate = (String) query.get("date1");
        queryEndDate = (String) query.get("date2");
        requestDimensions = (List<String>) query.get("dimensions");
        requestMetrics = (List<String>) query.get("metrics");
    }

    private String createSubQuery(Map<String, Object> response) {
        parseResponse(response);
        StringBuilder subRequest = new StringBuilder();
        subRequest.append(MetrikaUtils.JANDEX_DRILLDOWN).append(webpageId)
                .append("&group=day&metrics=").append(requestMetrics.stream().collect(Collectors.joining(",")))
                .append("&dimensions=").append(requestDimensions.stream().collect(Collectors.joining(",")))
                .append("&date1=").append(queryStatrtDate)
                .append("&date2=").append(queryEndDate).append("&limit=100000");
        return subRequest.toString();
    }

    private Map<String, List<String>> handleResponse(List<Map<String, List>> data, String subQuery) {
        Map<String, List<String>> insertQueriesMap = new HashMap<>();
        for (Map<String, List> entry: data) {
            String parentIdName = (String) ((Map)entry.get("dimension")).get("name");
            String subRequest = subQuery + "&parent_id=" + "[%22" + parentIdName +"%22]";
            handleSubRequest(subRequest, parentIdName, insertQueriesMap);
        }
        return insertQueriesMap;
    }

    private void handleSubRequest(String request, String parentIdName, Map<String, List<String>> insertQueriesMap) {
        Map<String, Object> response = parser.parse(fetcher.fetch(request));
        System.out.println("subrequest for " + parentIdName);
        List<Map<String, List>> data = (List) response.get("data");
        for (Map<String, List> entry: data) {
            String entryDate = (String) ((Map)entry.get("dimension")).get("name");
            List<Double> metrics = (List<Double>) entry.get("metrics");
            for (int i = 0; i < requestMetrics.size(); i++) {
                String query = "'" + entryDate + "', '" + parentIdName + "', " + metrics.get(i);
                updateInsertQueriesMap(i, query, insertQueriesMap);
            }
        }
    }

    private void updateInsertQueriesMap(int index, String query, Map<String, List<String>> insertQueriesMap) {
        List <String> insertList;
        if (insertQueriesMap.get(requestMetrics.get(index)) == null) {
            System.out.println("Creating new array list for " + requestMetrics.get(index));
            insertList = new ArrayList<>();
            insertList.add(query);
            insertQueriesMap.put(requestMetrics.get(index), insertList);
        } else {
            System.out.println("Adding to existing list " + requestMetrics.get(index));
            insertList = insertQueriesMap.get(requestMetrics.get(index));
            insertList.add(query);
            System.out.println("Adding to existing list " + insertQueriesMap.get(requestMetrics.get(index)));
        }
    }

    private String createFinalQuery(Map<String, List<String>> insertQueriesMap) {
        List<String> insertQueries = new ArrayList();
        for (String metrica: insertQueriesMap.keySet()) {
            String parsedInsertValues = insertQueriesMap.get(metrica).stream()
                    .map(value -> "(nextval('hibernate_sequence'), " + webpageId + ", " + value + ")")
                    .collect(Collectors.joining(",\n\t"));
            String insertQuery = CONSTANTS.getQueryMap().get(metrica);
            insertQueries.add(insertQuery + parsedInsertValues + ";");
        }
        return insertQueries.stream().collect(Collectors.joining("\n"));
    }

}
