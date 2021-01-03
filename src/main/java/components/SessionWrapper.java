package components;

import Implementation.SessionManagerImp;
import Interfaces.Wrappable;
import Interfaces.WrappableWithStringArg;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SessionWrapper {

    public static <T> T wrap(Wrappable<T> innerFucntion) {
        Session session = new SessionManagerImp().startSession();
        Transaction tx = session.beginTransaction();
        T result = innerFucntion.wrap(session);
        tx.commit();
        session.close();
        return result;
    }

    public static <T> T wrapWithStringArg(WrappableWithStringArg<T> innerFucntion, String arg) {
        Session session = new SessionManagerImp().startSession();
        Transaction tx = session.beginTransaction();
        T result = innerFucntion.wrap(session, arg);
        tx.commit();
        session.close();
        return result;
    }

}
