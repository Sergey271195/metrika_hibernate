package components;

import Interfaces.WrappableWithStringArg;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

public class UpdateQueryExecutor implements WrappableWithStringArg {

    private static void  __executeUpdateQuery__(Session session, String updateQuery) {
        NativeQuery query = session.createSQLQuery(updateQuery);
        query.executeUpdate();
    }

    @Override
    public Object wrap(Session session, String insertQuery) {
        __executeUpdateQuery__(session, insertQuery);
        return null;
    }

}
