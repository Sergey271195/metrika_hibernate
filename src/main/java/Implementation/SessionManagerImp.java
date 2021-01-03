package Implementation;

import Interfaces.SessionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionManagerImp implements SessionManager {

    public static SessionFactory sFactory = new MetadataSources(
            new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml").build()
    ).buildMetadata().buildSessionFactory();

    @Override
    public Session startSession() {
        Session session = sFactory.openSession();
        return session;
    }

    @Override
    public void endSession(Session session) {
        session.close();
    }

}
