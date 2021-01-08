package Sources.TrafficSource;

import java.util.HashMap;
import java.util.Map;

public class TrafficSourceConstants {

    public TrafficSourceConstants() {
        initializeQuerieMap();
    }

    public static String dimensions = "ym:s:lastsignTrafficSource";

    private static Map<String,String> queriesMap = new HashMap<>();

    private static String insertQuery =
            "INSERT INTO goalstrafficsource (id, webpage_id, date, goal_id, source_id, reaches)\nVALUES\n\t";
    private static String viewsInsertQuery =
            "INSERT INTO viewstrafficsource (id, webpage_id, date, source_id, reaches)\nVALUES\n\t";
    private static String purchasesInsertQuery =
            "INSERT INTO purchasestrafficsource (id, webpage_id, date, source_id, reaches)\nVALUES\n\t";
    private static String purchasedPriceInsertQuery =
            "INSERT INTO pricetrafficsource (id, webpage_id, date, source_id, reaches)\nVALUES\n\t";

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
