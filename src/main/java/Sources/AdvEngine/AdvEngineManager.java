package Sources.AdvEngine;

import Interfaces.SessionManager;
import Sources.Abstract.SourceManager;
import models.AdvEngine;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AdvEngineManager extends SourceManager<AdvEngine> {

    public AdvEngineManager(SessionManager sm) {
        super(sm, AdvEngine.class);
    }

    @Override
    public void updateSourcesMap() {
        List<AdvEngine> sourceList = this.fetchSourcesList();
        this.sources = sourceList.stream().collect(Collectors.toMap(AdvEngine::getId, Function.identity()));
    }

    @Override
    public void createNewSourceInstance(Map<String, List> responseData) {
        Map<String, String> dimensions = (Map) responseData.get("dimensions").get(0);
        if (!sourceExists(dimensions.get("id"))) {
            AdvEngine newAdvEngine = createNewAdvEngine(dimensions.get("id"), dimensions.get("name"));
            persistNewAdvEngine(newAdvEngine);
            updateSourcesMap();
        }
    }

    private void persistNewAdvEngine(AdvEngine newAdvEngine) {
        Session session = sm.startSession();
        Transaction tx = session.beginTransaction();
        session.persist(newAdvEngine);
        tx.commit();
        session.close();
    }

    private AdvEngine createNewAdvEngine(String id, String name) {
        AdvEngine newAdvEngine = new AdvEngine();
        newAdvEngine.setId(id);
        newAdvEngine.setName(name);
        return newAdvEngine;
    }
}
