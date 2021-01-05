import Implementation.FetcherImp;
import Implementation.JsonParserImp;
import Sources.AdvEngine.AdvEngineUpdater;
import Sources.Factory.SourceManagerFactory;
import Sources.ReferralSource.ReferralSourceUpdater;
import Sources.SearchEngine.SearchEngineUpdater;
import Implementation.SessionManagerImp;
import Interfaces.SessionManager;
import Sources.SocialNetwork.SocialNetworkUpdater;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDate;


public class Entrypoint {

    public static void main(String[] args) {

        SessionFactory sf = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml").build()
        ).buildMetadata().buildSessionFactory();
        Session session = sf.openSession();

        FetcherImp fetcher = new FetcherImp();
        JsonParserImp jsonParser = new JsonParserImp();
        JandexRequester requester = new JandexRequester(fetcher, jsonParser, session);

        SessionManager sessionManager = new SessionManagerImp();
        SourceManagerFactory sourceFactory = new SourceManagerFactory(sessionManager);

        //Traffic source update
        TrafficSourceUpdater tsUpdater = new TrafficSourceUpdater(fetcher, jsonParser, session);
        tsUpdater.updateDatabase();

        DatabaseUpdater dbUpdater = new DatabaseUpdater(fetcher, jsonParser, sourceFactory);
        dbUpdater.updateDatabase();

        DatabaseFiller dbFiller = new DatabaseFiller(fetcher, jsonParser, sourceFactory);
        dbFiller.fillUntilDate(LocalDate.parse("2021-01-04"));

        session.close();
    }

}
