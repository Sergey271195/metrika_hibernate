package components;

import models.Goal;

import javax.persistence.Table;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetrikaUtils {

    public static String JANDEX_STAT_BY_TIME = "https://api-metrika.yandex.net/stat/v1/data/bytime?ids=";

    public static List<List<Goal>> splitGoals(List<Goal> goals) {

        List<List<Goal>> splitedGoals = new ArrayList<>();
        int numberOfLists = goals.size()/15;
        for (int i = 0; i <= numberOfLists; i++) {
            int startIndex = 15*i;
            int endIndex = i == numberOfLists ? goals.size() : 15*(i+1);
            splitedGoals.add(goals.subList(startIndex, endIndex));
        }
        return splitedGoals;
    }

    public static String getTableName(Class tableClass) {
        String tableName = tableClass.isAnnotationPresent(Table.class)
                ? ((Table) tableClass.getAnnotation(Table.class)).name()
                : tableClass.getSimpleName();
        return tableName;
    }

    public static LocalDate convertToDate(ZonedDateTime dateTime) {
        String year = String.valueOf(dateTime.getYear());
        String month = dateTime.getMonthValue() >= 10 ?  String.valueOf(dateTime.getMonthValue()) : "0"+dateTime.getMonthValue();
        String day = dateTime.getDayOfMonth() >= 10 ?  String.valueOf(dateTime.getDayOfMonth()) : "0"+dateTime.getDayOfMonth();
        LocalDate date = LocalDate.parse(year+"-"+month+"-"+day);
        return date;
    }

    public static String getDimensionId(Map<String, List> responseData) {
        return (String) ((Map) responseData.get("dimensions").get(0)).get("id");
    }

    public static List<Double> getMetriksList(Map<String, List> responseData) {
        return (List) responseData.get("metrics").stream().map(goal -> ((List) goal).get(0)).collect(Collectors.toList());
    }

    public static Map<String, List> groupMetricsByDimensions(List<Map<String, List>> metrics) {
        return metrics.stream()
                .collect(Collectors.toMap(MetrikaUtils::getDimensionId, MetrikaUtils::getMetriksList, (a, b) -> a));
    }

}
