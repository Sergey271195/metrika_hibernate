package Sources.SearchEngine;

import java.util.HashMap;
import java.util.Map;

public class SearchEngineConstants {

    public SearchEngineConstants() {
        initializeQuerieMap();
    }

    public static String dimensions = "ym:s:lastsignSearchEngineRoot";

    private static Map<String,String> queriesMap = new HashMap<>();

    private static String insertQuery =
            "INSERT INTO goalssearchengine (id, webpage_id, goal_id, date, engine_id, reaches)\nVALUES\n\t";
    private static String viewsInsertQuery =
            "INSERT INTO viewssearchengine (id, webpage_id, date, engine_id, reaches)\nVALUES\n\t";
    private static String purchasesInsertQuery =
            "INSERT INTO purchasessearchengine (id, webpage_id, date, engine_id, reaches)\nVALUES\n\t";
    private static String purchasedPriceInsertQuery =
            "INSERT INTO pricesearchengine (id, webpage_id, date, engine_id, reaches)\nVALUES\n\t";

    private static void initializeQuerieMap() {
        queriesMap.put("ym:s:visits", viewsInsertQuery);
        queriesMap.put("ym:s:ecommercePurchases", purchasesInsertQuery);
        queriesMap.put("ym:s:productPurchasedPrice", purchasedPriceInsertQuery);
        queriesMap.put("ym:s:goalReaches", insertQuery);
    }

    public Map<String,String> getQueryMap() {
        return queriesMap;
    }

}
