package Sources.AdvEngine;

import java.util.HashMap;
import java.util.Map;

public class AdvEngineConstants {

    public AdvEngineConstants() {
        initializeQuerieMap();
    }

    public static String dimensions = "ym:s:lastsignAdvEngine";

    private static Map<String,String> queriesMap = new HashMap<>();

    private static String insertQuery =
            "INSERT INTO goalsadvengine (id, webpage_id, goal_id, date, adv_id, reaches)\nVALUES\n\t";
    private static String viewsInsertQuery =
            "INSERT INTO viewsadvengine (id, webpage_id, date, adv_id, reaches)\nVALUES\n\t";
    private static String purchasesInsertQuery =
            "INSERT INTO purchasesadvengine (id, webpage_id, date, adv_id, reaches)\nVALUES\n\t";
    private static String purchasedPriceInsertQuery =
            "INSERT INTO priceadvengine (id, webpage_id, date, adv_id, reaches)\nVALUES\n\t";

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
