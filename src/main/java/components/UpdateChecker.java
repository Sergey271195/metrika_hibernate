package components;

import Implementation.SessionManagerImp;
import Interfaces.Wrappable;
import managers.WebpageManager;
import models.Webpage;
import org.hibernate.Session;
import org.hibernate.type.LongType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateChecker implements Wrappable<Boolean> {

    private final Class tableClass;
    private List<Webpage> updateList;
    public static LocalDate yesterday = LocalDate.now().minusDays(1);

    public UpdateChecker(Class tableClass) { this.tableClass = tableClass; }

    private String getCheckUpdateQuery() {
        String tableName = MetrikaUtils.getTableName(tableClass);
        StringBuilder checkForUpdate = new StringBuilder();
        checkForUpdate
                .append("WITH update AS (SELECT DISTINCT webpage_id AS wid FROM ").append(tableName)
                .append(" WHERE date = '" ).append(yesterday)
                .append("' GROUP BY webpage_id) SELECT DISTINCT pageId ")
                .append("FROM webpage WHERE actual = true AND pageId NOT IN (SELECT wid FROM update);");
        return checkForUpdate.toString();
    }

    private Boolean __dbIsUpToDate__(Session session) {
        String checkUpdateQuery = getCheckUpdateQuery();
        List<Long> updateList = session.createSQLQuery(checkUpdateQuery)
                .addScalar("pageId", new LongType()).list();
        List<Long> filteredUpdateList = updateList.stream()
                .filter(id -> !WebpageManager.wpWithoutGoalsMap.containsKey(id))
                .collect(Collectors.toList());
        setWebpagesForUpdate(filteredUpdateList);
        return filteredUpdateList.size() == 0;
    }

    private void setWebpagesForUpdate(List<Long> filteredUpdateList) {
        Session session = new SessionManagerImp().startSession();
        this.updateList = session.byMultipleIds(Webpage.class)
                .enableSessionCheck(true).multiLoad(filteredUpdateList);
        session.close();
    }

    public List<Webpage> getUpdateList() {
        return updateList;
    }

    @Override
    public Boolean wrap(Session session) {
        return this.__dbIsUpToDate__(session);
    }
}
