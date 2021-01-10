import Interfaces.SessionManager;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class NecessaryQueries {

    private final SessionManager sessionManager;

    NecessaryQueries(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public String createSelectQueryForCounterPeriods() {
        Session session = sessionManager.startSession();
        Query query = session.createQuery(
                "SELECT webpage_id, source_id, sum(reaches) FROM ViewsByTrafficSource AS s " +
                        "WHERE date BETWEEN '2021-01-01' AND '2021-01-09' GROUP BY webpage_id, source_id"
        );
        List result = query.list();
        for (Object r : result) {
            System.out.println(r);
        }

        return null;
    }

}
