package Sources.Abstract;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import components.MetrikaUtils;
import components.SessionWrapper;
import components.UpdateChecker;
import components.UpdateQueryExecutor;
import models.Webpage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class DatabaseViewsUpdaterAbs {

    private final Class tableClass;
    private final Fetcher fetcher;
    private final JsonParser parser;
    private final String dimensions;
    private UpdateChecker checker;

    protected static LocalDate yesterday = UpdateChecker.yesterday;

    public DatabaseViewsUpdaterAbs(
            Class tableClass, String dimensions, Fetcher fetcher, JsonParser parser
    ) {
        this.tableClass = tableClass;
        this.dimensions = dimensions;
        this.fetcher = fetcher;
        this.parser = parser;
        this.checker = new UpdateChecker(tableClass);
    }

    public void updateDatabase() {
        if (!dbIsUpToDate()) {
            List<Webpage> webpagesToUpdate = checker.getUpdateList();

            webpagesToUpdate.stream()
                    .peek(w -> System.out.println(this.getClass().getSimpleName() + " - " + w.getPageId() + " - " + w.getName()))
                    .map(webpage -> fetchDataFromMetirka(webpage))
                    .map(response -> createInsertQuery(response)).filter(Objects::nonNull)
                    .forEach(insertQuery -> executeInsertQuery(insertQuery));
        } else {
            System.out.println(tableClass.getSimpleName() + " is up-to-date");
        }
    }

    private Map<String, Object> fetchDataFromMetirka(Webpage webpage) {
        String request = webpage.isCommercial()
                ? createViewsAndCommerceRequest(webpage)
                : createViewsRequest(webpage);
        Map<String, Object> response = parser.parse(fetcher.fetch(request));
        return response;
    }

    private String createViewsRequest(Webpage webpage) {

        StringBuilder request = new StringBuilder();
        request.append(MetrikaUtils.JANDEX_STAT_BY_TIME).append(webpage.getPageId())
                .append("&group=day&metrics=ym:s:visits")
                .append(requestEndingLine());

        return request.toString();
    }

    private String createViewsAndCommerceRequest(Webpage webpage) {

        StringBuilder request = new StringBuilder();
        request.append(MetrikaUtils.JANDEX_STAT_BY_TIME).append(webpage.getPageId())
                .append("&group=day&metrics=ym:s:visits,ym:s:ecommercePurchases,ym:s:productPurchasedPrice")
                .append(requestEndingLine());

        return request.toString();
    }

    private String requestEndingLine() {
        StringBuilder requestEnd = new StringBuilder();
        requestEnd.append("&dimensions=").append(dimensions)
                .append("&date1=").append(yesterday)
                .append("&date2=").append(yesterday);
        return requestEnd.toString();
    }

    private String createInsertQuery(Map<String, Object> response) {

        List<Map<String, List>> data = (List) response.get("data");
        Map<String, Object> query = (Map<String, Object>) response.get("query");
        List<String> metrics = (List) query.get("metrics");
        Long webpageId = (Long) ((List) query.get("ids")).get(0);

        return createStatement(data, metrics, webpageId);
    }

    private void executeInsertQuery(String insertQuery) {
        SessionWrapper.wrapWithStringArg(new UpdateQueryExecutor(), insertQuery);
    }

    private boolean dbIsUpToDate() { return SessionWrapper.wrap(checker); };

    protected abstract String createStatement(List<Map<String, List>> data, List<String> metrics, Long webpageId);
}
