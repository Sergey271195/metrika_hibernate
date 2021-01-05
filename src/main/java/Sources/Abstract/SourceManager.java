package Sources.Abstract;
import Interfaces.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class SourceManager<T> {

    public Map<String, T> sources;
    private final Class className;
    protected final SessionManager sm;

    public SourceManager(SessionManager sm, Class className) {
        this.sm = sm;
        this.className = className;
        updateSourcesMap();
    }

    public abstract void updateSourcesMap();
    public abstract void createNewSourceInstance(Map<String, List> responseData);

    protected boolean sourceExists(String sourceId) {
        return sources.containsKey(sourceId);
    }

    protected List<T> fetchSourcesList() {
        Session session = sm.startSession();
        Transaction tx = session.beginTransaction();
        List<T> sourceList = session.createQuery("FROM " + className.getSimpleName()).list();
        tx.commit();
        sm.endSession(session);
        return sourceList;
    }

    public List<String> mapGoalsToSource(List<Map<String, List>> metrics, List<Long> goals) {
        List<String> insertValues = null;
        Map<String, List> sourceList = groupMetricsByDimensions(metrics);
        for (String source: sourceList.keySet()) {
            insertValues = IntStream.range(0, goals.size())
                    .mapToObj(i -> goals.get(i) + ", '" + source + "', " + sourceList.get(source).get(i))
                    .collect(Collectors.toList());
        }
        return insertValues;
    }

    private static String getDimensionId(Map<String, List> responseData) {
        String id = (String) ((Map) responseData.get("dimensions").get(0)).get("id");
        String name = (String) ((Map) responseData.get("dimensions").get(0)).get("name");
        return id == null ? name : id;
    }

    private static List<Double> getMetriksList(Map<String, List> responseData) {
        return (List) responseData.get("metrics").stream().map(goal -> ((List) goal).get(0)).collect(Collectors.toList());
    }

    private Map<String, List> groupMetricsByDimensions(List<Map<String, List>> metrics) {
        return metrics.stream().peek(source -> createNewSourceInstance(source))
                .collect(Collectors.toMap(SourceManager::getDimensionId, SourceManager::getMetriksList, (a, b) -> a));
    }

}
