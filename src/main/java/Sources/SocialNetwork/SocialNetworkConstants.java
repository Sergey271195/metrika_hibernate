package Sources.SocialNetwork;

import java.util.HashMap;
import java.util.Map;

public class SocialNetworkConstants {

    public SocialNetworkConstants() {
        initializeQuerieMap();
    }

    public static String dimensions = "ym:s:lastsignSocialNetwork";

    private static Map<String,String> queriesMap = new HashMap<>();

    private static String insertQuery =
            "INSERT INTO goalssocialnetwork (id, webpage_id, date, goal_id, network_id, reaches)\nVALUES\n\t";
    private static String viewsInsertQuery =
            "INSERT INTO viewssocialnetwork (id, webpage_id, date, network_id, reaches)\nVALUES\n\t";
    private static String purchasesInsertQuery =
            "INSERT INTO purchasessocialnetwork (id, webpage_id, date, network_id, reaches)\nVALUES\n\t";
    private static String purchasedPriceInsertQuery =
            "INSERT INTO pricesocialnetwork (id, webpage_id, date, network_id, reaches)\nVALUES\n\t";

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
