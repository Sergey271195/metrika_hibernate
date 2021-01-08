package Sources.ReferralSource;

import java.util.HashMap;
import java.util.Map;

public class ReferralSourceConstants {

    public ReferralSourceConstants() {
        initializeQuerieMap();
    }

    private static String dimensions = "ym:s:lastsignReferalSource";

    private static Map<String,String> queriesMap = new HashMap<>();

    private static String insertQuery =
            "INSERT INTO goalsreferral (id, webpage_id, goal_id, date, referral_id, reaches)\nVALUES\n\t";
    private static String viewsInsertQuery =
            "INSERT INTO viewsreferral (id, webpage_id, date, referral_id, reaches)\nVALUES\n\t";
    private static String purchasesInsertQuery =
            "INSERT INTO purchasesreferral (id, webpage_id, date, referral_id, reaches)\nVALUES\n\t";
    private static String purchasedPriceInsertQuery =
            "INSERT INTO pricereferral (id, webpage_id, date, referral_id, reaches)\nVALUES\n\t";

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
