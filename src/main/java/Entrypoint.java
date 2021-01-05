import Implementation.FetcherImp;
import Implementation.JsonParserImp;
import Sources.ReferralSource.ReferralSourceFiller;
import Sources.ReferralSource.ReferralSourceManager;
import Sources.ReferralSource.ReferralSourceUpdater;
import Sources.SearchEngine.SearchEngineFiller;
import Sources.SearchEngine.SearchEngineUpdater;
import Implementation.SessionManagerImp;
import Interfaces.SessionManager;
import Sources.SearchEngine.SearchEngineManager;
import Sources.SocialNetwork.SocialNetworkFiller;
import Sources.SocialNetwork.SocialNetworkManager;
import Sources.SocialNetwork.SocialNetworkUpdater;
import models.ReferralSource;
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

        //Traffic source update
        DatabaseUpdater updater = new DatabaseUpdater(fetcher, jsonParser, session);
        updater.updateDatabase();

        //Search engine update
        SearchEngineUpdater seUpdater = new SearchEngineUpdater(sessionManager, fetcher, jsonParser);
        seUpdater.updateDatabase();

        //Social network update
        SocialNetworkUpdater snUpdater = new SocialNetworkUpdater(sessionManager, fetcher, jsonParser);
        snUpdater.updateDatabase();

        //Referral source update
        ReferralSourceUpdater rsUpdater = new ReferralSourceUpdater(sessionManager, fetcher, jsonParser);
        rsUpdater.updateDatabase();

        //Search engine filler
        //SearchEngineManager seManager = new SearchEngineManager(sessionManager);
        //SearchEngineFiller seFiller = new SearchEngineFiller(seManager, LocalDate.parse("2021-01-02"), fetcher, jsonParser);
        //seFiller.fillDatabase();

        //Social network filler
        //SocialNetworkManager snManager = new SocialNetworkManager(sessionManager);
        //SocialNetworkFiller snFiller = new SocialNetworkFiller(snManager, LocalDate.parse("2021-01-03"), fetcher, jsonParser);
        //snFiller.fillDatabase();

        //Referral source filler
        ReferralSourceManager rsManager = new ReferralSourceManager(sessionManager);
        ReferralSourceFiller rsFiller = new ReferralSourceFiller(rsManager, LocalDate.parse("2021-01-03"), fetcher, jsonParser);
        rsFiller.fillDatabase();

        session.close();
    }

}
