package Sources.SearchEngine;

import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.Abstract.DatabaseFiller;
import Sources.Abstract.SourceManager;
import components.MetrikaUtils;
import models.SearchEngine;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchEngineFiller extends DatabaseFiller {

    private SourceManager sourceManager;
    private static String dimensions = "ym:s:lastsignSearchEngineRoot";

    private static String insertQuery =
            "INSERT INTO goalssearchengine (id, webpage_id, date, goal_id, engine_id, reaches)\nVALUES\n\t";

    public SearchEngineFiller(SourceManager sourceManager,
                              String dimensions, LocalDate endDate,
                              JsonParser parser, Fetcher fetcher)
    {
        super(dimensions, endDate, parser, fetcher);
        this.sourceManager = sourceManager;
    }

    @Override
    protected String createStatement(List<Map<String, List>> data, List timeStamp) {
        Map<String, List> valuesMap = data.stream().peek(source -> sourceManager.createNewSourceInstance(source))
                .collect(Collectors.toMap(MetrikaUtils::getDimensionId, MetrikaUtils::getMetriksList));
        //valuesMap.keySet().stream().peek(key)
        return null;
    }
}
