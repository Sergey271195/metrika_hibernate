package Sources.Abstract;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import components.MetrikaUtils;
import components.SessionWrapper;
import components.UpdateQueryExecutor;
import managers.WebpageManager;
import models.Webpage;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DatabaseViewsFiller {

    private final String dimensions;
    private final LocalDate endDate;
    private final Fetcher fetcher;
    private final JsonParser parser;

    private static String nonCommercialMetrics = "&group=day&metrics=ym:s:visits";
    private static String commercialMetrics = nonCommercialMetrics + ",ym:s:ecommercePurchases,ym:s:productPurchasedPrice";


    public DatabaseViewsFiller(String dimensions, LocalDate endDate, JsonParser parser, Fetcher fetcher) {
        this.dimensions = dimensions;
        this.endDate = endDate;
        this.parser = parser;
        this.fetcher = fetcher;
    }

    public void fillDatabase() {
        List<Webpage> webpages = new WebpageManager().getWebpagesFromDB();
        //List<Long> updated = Arrays.asList(59162569L, 62401888L, 19915630L);
        /*for (Webpage webpage: webpages) {
            if (!updated.contains(webpage.getPageId())) {
                System.out.println(webpage.getName() + " - " + webpage.getPageId());
                Map<String, Object> response = fecthHistoryDataFromMetrika(webpage);
                String insertQuery = createInsertQuery(response);
                executeInsertQuery(insertQuery);
            }
        }*/
        webpages.stream()
                .filter(webpage -> webpage.getPageId() == 49911565L)
                .peek(w -> System.out.println(this.getClass().getSimpleName() + " - " + w.getPageId() + " - " + w.getName() + " - " + w.getCreateTime()))
                .map(webpage -> fecthHistoryDataFromMetrika(webpage))
                .peek(resp -> System.out.println(resp))
                .map(this::createInsertQuery).filter(Objects::nonNull)
                .peek(query -> System.out.println(query))
                //.forEach(query -> System.out.println(query));
                .forEach(this::executeInsertQuery);
    }

    private Map<String, Object> fecthHistoryDataFromMetrika(Webpage webpage) {
        String baseUrl = this.dimensions.contains("ym:s:lastsignReferalSource")
                ? MetrikaUtils.JANDEX_DRILLDOWN
                : MetrikaUtils.JANDEX_STAT_BY_TIME;
        String request = createHistoryRequest(webpage, baseUrl);
        Map<String, Object> response = parser.parse(fetcher.fetch(request));
        return response;
    }

    private String createViewsRequest(Webpage webpage, String baseUrl) {
        StringBuilder request = new StringBuilder();
        request.append(baseUrl).append(webpage.getPageId())
                .append(nonCommercialMetrics)
                .append(requestHistoryEndingLine(webpage));
        return request.toString();
    }

    private String createViewsAndCommerceRequest(Webpage webpage, String baseUrl) {
        StringBuilder request = new StringBuilder();
        request.append(baseUrl).append(webpage.getPageId())
                .append(commercialMetrics)
                .append(requestHistoryEndingLine(webpage));
        return request.toString();
    }

    private String createHistoryRequest(Webpage webpage, String baseUrl) {
        String request = webpage.isCommercial()
                ? createViewsAndCommerceRequest(webpage, baseUrl)
                : createViewsRequest(webpage, baseUrl);
        return request;
    }

    private String requestHistoryEndingLine(Webpage webpage) {
        LocalDate creationDate = MetrikaUtils.convertToDate(webpage.getCreateTime()).plusDays(1);
        StringBuilder requestEnd = new StringBuilder();
        requestEnd.append("&dimensions=").append(dimensions)
                .append("&date1=").append(creationDate)
                .append("&date2=").append(endDate).append("&limit=100000");
        return requestEnd.toString();
    }

    protected abstract String createInsertQuery(Map<String, Object> response);

    private void executeInsertQuery(String insertQuery) {
        SessionWrapper.wrapWithStringArg(new UpdateQueryExecutor(), insertQuery);
    }

}
