package Sources.SearchPhrase;

import java.util.HashMap;
import java.util.Map;

public class SearchPhraseConstants {

    public SearchPhraseConstants() {
        initializeQuerieMap();
    }

    public static String dimensions = "ym:s:lastsignSearchPhrase";

    private static Map<String,String> queriesMap = new HashMap<>();

    private static String insertQuery =
            "INSERT INTO goalssearchphrase (id, webpage_id, goal_id, date, phrase_id, reaches)\nVALUES\n\t";
    private static String viewsInsertQuery =
            "INSERT INTO viewssearchphrase (id, webpage_id, date, phrase_id, reaches)\nVALUES\n\t";
    private static String purchasesInsertQuery =
            "INSERT INTO purchasessearchphrase (id, webpage_id, date, phrase_id, reaches)\nVALUES\n\t";
    private static String purchasedPriceInsertQuery =
            "INSERT INTO pricesearchphrase (id, webpage_id, date, phrase_id, reaches)\nVALUES\n\t";

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
