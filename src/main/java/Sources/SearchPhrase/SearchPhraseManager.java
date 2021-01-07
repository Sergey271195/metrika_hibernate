package Sources.SearchPhrase;

import Interfaces.SessionManager;
import Sources.Abstract.SourceManager;
import models.sources.SearchPhrase;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class SearchPhraseManager extends SourceManager<SearchPhrase> {

    public SearchPhraseManager(SessionManager sm) {
        super(sm, SearchPhrase.class);
    }

    @Override
    public void updateSourcesMap() {
        List<SearchPhrase> sourceList = this.fetchSourcesList();
        this.sources = sourceList.stream().collect(Collectors.toMap(SearchPhrase::getId, Function.identity()));
    }

    @Override
    public void createNewSourceInstance(Map<String, List> responseData) {
        Map<String, String> dimensions = (Map) responseData.get("dimensions").get(0);
        if (!sourceExists(dimensions.get("name"))) {
            SearchPhrase newSearchPhrase = createNewSearchPhrase(dimensions.get("name"), dimensions.get("name"));
            persistNewSearchPhrase(newSearchPhrase);
            updateSourcesMap();
        }
    }

    private void persistNewSearchPhrase(SearchPhrase newSearchPhrase) {
        Session session = sm.startSession();
        Transaction tx = session.beginTransaction();
        session.persist(newSearchPhrase);
        tx.commit();
        session.close();
    }

    private SearchPhrase createNewSearchPhrase(String id, String name) {
        SearchPhrase newSearchPhrase = new SearchPhrase();
        newSearchPhrase.setId(id);
        newSearchPhrase.setName(name);
        return newSearchPhrase;
    }
}
