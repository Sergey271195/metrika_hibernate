package Sources.TrafficSource;

import Interfaces.SessionManager;
import Sources.Abstract.SourceManager;
import models.sources.TrafficSource;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TrafficSourceManager extends SourceManager<TrafficSource> {

    public TrafficSourceManager(SessionManager sm) {
        super(sm, TrafficSource.class);
    }

    @Override
    public void updateSourcesMap() {
        List<TrafficSource> sourceList = this.fetchSourcesList();
        this.sources = sourceList.stream().collect(Collectors.toMap(TrafficSource::getId, Function.identity()));
    }

    @Override
    public void createNewSourceInstance(Map<String, List> responseData) {
        Map<String, String> dimensions = (Map) responseData.get("dimensions").get(0);
        if (!sourceExists(dimensions.get("id"))) {
            TrafficSource newTrafficSource = createNewTrafficSource(dimensions.get("id"), dimensions.get("name"));
            persistNewTrafficSource(newTrafficSource);
            updateSourcesMap();
        }
    }

    private void persistNewTrafficSource(TrafficSource newTrafficSource) {
        Session session = sm.startSession();
        Transaction tx = session.beginTransaction();
        session.persist(newTrafficSource);
        tx.commit();
        session.close();
    }

    private TrafficSource createNewTrafficSource(String id, String name) {
        TrafficSource newTrafficSource = new TrafficSource();
        newTrafficSource.setId(id);
        newTrafficSource.setName(name);
        return newTrafficSource;
    }

}
