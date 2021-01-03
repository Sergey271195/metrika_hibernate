package Interfaces;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface SessionManager {

    Session startSession();
    void endSession(Session session);

}
