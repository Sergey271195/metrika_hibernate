package Sources.SocialNetwork;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.Abstract.DatabaseViewsUpdaterAbs;
import Sources.Abstract.SourceManager;
import models.views.ViewsBySocialNetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SocialNetworkViewsUpdater extends DatabaseViewsUpdaterAbs {

    private SourceManager sourceManager;
    private static String dimensions = "ym:s:lastsignSocialNetwork";

    private static String viewsInsertQuery =
            "INSERT INTO viewssocialnetwork (id, webpage_id, date, network_id, reaches)\nVALUES\n\t";
    private static String purchasesInsertQuery =
            "INSERT INTO purchasessocialnetwork (id, webpage_id, date, network_id, reaches)\nVALUES\n\t";
    private static String purchasedPriceInsertQuery =
            "INSERT INTO pricesocialnetwork (id, webpage_id, date, network_id, reaches)\nVALUES\n\t";

    private static Map<String, String> queriesMap = new HashMap<>();

    public SocialNetworkViewsUpdater(SourceManager sourceManager, Fetcher fetcher, JsonParser parser) {
        super(ViewsBySocialNetwork.class, dimensions, fetcher, parser);
        this.sourceManager = sourceManager;
        initializeQueriesMap();
    }

    private static void initializeQueriesMap() {
        queriesMap.put("ym:s:visits", viewsInsertQuery);
        queriesMap.put("ym:s:ecommercePurchases", purchasesInsertQuery);
        queriesMap.put("ym:s:productPurchasedPrice", purchasedPriceInsertQuery);
    }

    @Override
    protected String createStatement(List<Map<String, List>> data, List<String> metrics, Long webpageId) {
        String insertQuery = null;
        if (data.size() != 0) {
            Map<String, List<String>> insertValues = sourceManager.mapMetricsToSource(data, metrics);
            insertQuery = createNonEmptyInsertQuery(insertValues, webpageId);
        } else {
            insertQuery =  createEmptyInsertQuery(webpageId);
        }
        return insertQuery;
    }

    private String createNonEmptyInsertQuery(Map<String, List<String>> insertValues, Long webpageId) {
        String insertQuery = insertValues.keySet().stream()
                .map(key ->
                        queriesMap.get(key) + createNonEmptyQueryForMetric(insertValues.get(key), webpageId) +  ";"
                ).collect(Collectors.joining("\n"));
        return insertQuery;
    }

    private String createNonEmptyQueryForMetric(List<String> insertValuesForMetric, Long webpageId) {
        return insertValuesForMetric.stream()
                .map(value ->
                        "(nextval('hibernate_sequence'), " + webpageId + ", '" + yesterday + "', " + value + ")"
                ).collect(Collectors.joining(",\n  \t\t"));
    }

    private String createEmptyInsertQuery(Long webpageId) {
        String values =  "(nextval('hibernate_sequence'), " + webpageId + ", '" + yesterday  + "', 'vkontakte', 0)";
        return viewsInsertQuery + values + ";";
    }
}
