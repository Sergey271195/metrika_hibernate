package Sources.SearchEngine;

import Interfaces.SessionManager;
import Sources.Abstract.SourceManager;
import models.SearchEngine;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SearchEngineManager extends SourceManager<SearchEngine> {

    public SearchEngineManager(SessionManager sm) {
        super(sm, SearchEngine.class);
    }

    @Override
    public void updateSourcesMap() {
        List<SearchEngine> sourceList = this.fetchSourcesList();
        this.sources = sourceList.stream().collect(Collectors.toMap(SearchEngine::getId, Function.identity()));
    }

    @Override
    public void createNewSourceInstance(Map<String, List> responseData) {
        Map<String, String> dimensions = (Map) responseData.get("dimensions").get(0);
        if (!sourceExists(dimensions.get("id"))) {
            SearchEngine newEngine = createNewSearchEngine(dimensions.get("id"), dimensions.get("name"));
            persistNewEngine(newEngine);
            updateSourcesMap();
        }
    }

    private void persistNewEngine(SearchEngine newEngine) {
        Session session = sm.startSession();
        Transaction tx = session.beginTransaction();
        session.persist(newEngine);
        tx.commit();
        session.close();
    }

    private SearchEngine createNewSearchEngine(String id, String name) {
        SearchEngine newEngine = new SearchEngine();
        newEngine.setId(id);
        newEngine.setName(name);
        return newEngine;
    }

}
