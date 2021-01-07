import Implementation.FetcherImp;
import Implementation.JsonParserImp;
import Sources.Factory.SourceManagerFactory;
import Implementation.SessionManagerImp;
import Interfaces.SessionManager;
import managers.WebpageManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDate;
import java.util.List;


public class Entrypoint {

    public static void main(String[] args) {

        FetcherImp fetcher = new FetcherImp();
        JsonParserImp jsonParser = new JsonParserImp();

        SessionManager sessionManager = new SessionManagerImp();
        SourceManagerFactory sourceFactory = new SourceManagerFactory(sessionManager);

        DatabaseUpdater dbUpdater = new DatabaseUpdater(fetcher, jsonParser, sourceFactory);
        //dbUpdater.updateDatabase();

        DatabaseFiller dbFiller = new DatabaseFiller(fetcher, jsonParser, sourceFactory);
        dbFiller.fillUntilDate(LocalDate.parse("2021-01-05"));

        List<Long> ids = WebpageManager.getCommercialWebpagesIds(sessionManager);
        System.out.println(ids);
    }

}
