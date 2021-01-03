package managers;

import java.util.*;
import java.util.stream.Collectors;

public class TrafficSourceManager {

    public enum trafficSources {
            ORGANIC, INTERNAL, AD, REFERRAL, SOCIAL, DIRECT, RECOMMEND
    }

    public static EnumMap<trafficSources, String> trafficSourcesMap =
            Arrays.stream(trafficSources.values()).collect(Collectors.toMap(
                    value -> value, value -> value.toString().toLowerCase(),
                    (a, b) -> a,
                    () -> new EnumMap<>(trafficSources.class)
            ));

    public static List<String> trafficList =
            Arrays.asList("organic", "internal", "ad", "referral", "social", "direct", "recommend");


    public static Map<Long, String> mapGoalsToTrafficSource(List<Long> goals, List<Map<String, List>> metrics) {
        Map<String, List> metricsByDimensions = groupMetricsByDimensions(metrics);
        Map<Long, String> result = new HashMap<>();
        for (int i = 0; i < goals.size(); i++) {
            int index = i;
            List<Double> metrikaValues = trafficList.stream()
                    .map(trafficSource -> getDoubleValueFromResponseMap(metricsByDimensions, trafficSource, index))
                    .collect(Collectors.toList());
            Double total = metrikaValues.stream().reduce(0d, (a, b) -> a + b);
            metrikaValues.add(total);
            String insertValues = metrikaValues.stream()
                    .map(doubleValue -> String.valueOf(doubleValue)).collect(Collectors.joining(", "));
            result.put(goals.get(i), insertValues);
        }
        return result;
    }

    public static Double getDoubleValueFromResponseMap(Map<String, List> data, String key, int index) {
        return data.get(key) == null ? 0 :(Double) data.get(key).get(index);
    }

    public static String getDimensionName(Map<String, List> dimension) {
        return (String) ((Map) dimension.get("dimensions").get(0)).get("id");
    }

    public static List getMetriksList(Map<String, List> metrics) {
        return (List) metrics.get("metrics").stream().map(value -> ((List) value).get(0)).collect(Collectors.toList());
    }

    public static Map<String, List> groupMetricsByDimensions(List<Map<String, List>> metrics) {
        return metrics.stream().collect(Collectors.toMap(
                TrafficSourceManager::getDimensionName, TrafficSourceManager::getMetriksList, (a, b) -> a
        ));
    }

}
