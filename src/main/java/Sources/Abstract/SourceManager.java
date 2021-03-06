package Sources.Abstract;
import Interfaces.SessionManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.Array;
import java.util.*;
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

    public List<String> mapGoalsToSource(List<Map<String, List>> data, List<Long> goals) {
        List<String> insertValues = new ArrayList<>();
        Map<String, List> sourceList = groupMetricsByDimensions(data);
        for (String source: sourceList.keySet()) {
            List<String> intermediateValues = IntStream.range(0, goals.size())
                    .mapToObj(i -> goals.get(i) + ", '" + source + "', " + sourceList.get(source).get(i))
                    .collect(Collectors.toList());
            insertValues.addAll(intermediateValues);
        }
        return insertValues;
    }

    public Map<String, List<String>> mapMetricsToSource(List<Map<String, List>> data, List<String> metrics) {
        Map<String, List<String>> mappedData = new HashMap<>();
        for (Map<String, List> dataEntry: data) {
            createNewSourceInstance(dataEntry);
            String dimensionId = getDimensionId(dataEntry);
            for (int i = 0; i < metrics.size(); i++) {
                Double reaches = (Double) ((List) dataEntry.get("metrics").get(i)).get(0);
                if (mappedData.get(metrics.get(i)) != null) {
                    mappedData.get(metrics.get(i)).add("'" + dimensionId.replace("'", "''") + "'" + ", " + reaches);
                } else {
                    List<String> newList = new ArrayList<>();
                    newList.add("'" + dimensionId + "'" + ", " + reaches);
                    mappedData.put(metrics.get(i), newList);
                }
            }
        }
        return mappedData;
    }

    public Map<String, Map<String, List>> mapMetricsToSourceHistory(List<Map<String, List>> data, List<String> metrics) {
        Map<String, Map<String, List>> mappedData = new HashMap<>();
        for (Map<String, List> dataEntry: data) {
            createNewSourceInstance(dataEntry);
            String dimensionId = getDimensionId(dataEntry);
            for (int i = 0; i < metrics.size(); i++) {
                List<Double> reaches = (List<Double>) dataEntry.get("metrics").get(i);
                if (mappedData.get(metrics.get(i)) != null) {
                    mappedData.get(metrics.get(i)).put(dimensionId, reaches);
                } else {
                    Map<String, List> innerMappedData = new HashMap<>();
                    innerMappedData.put(dimensionId, reaches);
                    mappedData.put(metrics.get(i), innerMappedData);
                }
            }
        }
        return mappedData;
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
