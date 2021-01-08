package Sources.Abstract;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import components.MetrikaUtils;
import components.SessionWrapper;
import components.UpdateQueryExecutor;
import managers.WebpageManager;
import models.Webpage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class DatabaseViewsFiller {

    private final String dimensions;
    private final LocalDate endDate;
    private final Fetcher fetcher;
    private final JsonParser parser;

    private static String commercialMetrics = "&group=day&metrics=ym:s:visits,ym:s:ecommercePurchases,ym:s:productPurchasedPrice";
    private static String nonCommercialMetrics = "&group=day&metrics=ym:s:visits";

    public DatabaseViewsFiller(String dimensions, LocalDate endDate, JsonParser parser, Fetcher fetcher) {
        this.dimensions = dimensions;
        this.endDate = endDate;
        this.parser = parser;
        this.fetcher = fetcher;
    }

    public void fillDatabase() {
        List<Webpage> webpages = new WebpageManager().getWebpagesFromDB();
        webpages.stream().peek(w -> System.out.println(w.getPageId() + " - " + w.getName()))
                .map(webpage -> fecthHistoryDataFromMetrika(webpage))
                .peek(w -> System.out.println(w))
                .map(this::createInsertQuery).filter(Objects::nonNull)
                .forEach(this::executeInsertQuery);
    }

    public Map<String, Object> fecthHistoryDataFromMetrika(Webpage webpage) {
        String request = createHistoryRequest(webpage);
        Map<String, Object> response = parser.parse(fetcher.fetch(request));
        return response;
    }

    private String createViewsRequest(Webpage webpage) {
        StringBuilder request = new StringBuilder();
        request.append(MetrikaUtils.JANDEX_STAT_BY_TIME).append(webpage.getPageId())
                .append(nonCommercialMetrics)
                .append(requestHistoryEndingLine(webpage));
        return request.toString();
    }

    private String createViewsAndCommerceRequest(Webpage webpage) {
        StringBuilder request = new StringBuilder();
        request.append(MetrikaUtils.JANDEX_STAT_BY_TIME).append(webpage.getPageId())
                .append(commercialMetrics)
                .append(requestHistoryEndingLine(webpage));
        return request.toString();
    }

    public String createHistoryRequest(Webpage webpage) {
        String request = webpage.isCommercial()
                ? createViewsAndCommerceRequest(webpage)
                : createViewsRequest(webpage);
        return request;
    }

    public String requestHistoryEndingLine(Webpage webpage) {
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
