import Implementation.FetcherImp;
import Implementation.JsonParserImp;
import Sources.SearchEngine.SearchEngineUpdater;
import Implementation.SessionManagerImp;
import Interfaces.SessionManager;
import Sources.SearchEngine.SearchEngineManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


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

        SessionManager sm = new SessionManagerImp();
        SearchEngineManager sem = new SearchEngineManager(sm);
        SearchEngineUpdater seup = new SearchEngineUpdater(sm, fetcher, jsonParser);
        seup.updateDatabase();

        //Goal result = (Goal) session.createSQLQuery("SELECT * FROM goal WHERE goalId = 63506995;").addEntity(Goal.class).getSingleResult();
        //System.out.println(result.getName());

        DatabaseUpdater updater = new DatabaseUpdater(fetcher, jsonParser, session);
        updater.updateDatabase();

        /*List<Long> list = Arrays.asList(65064478L, 67865518L,65156065L,56247157L,53667862L,37960820L,42267299L,58976794L,67426333L,69704290L);
        System.out.println(list);
        List<Webpage> webpages =  WebpageManager.fetchWebpagesFromDB(session);
        webpages.stream().filter(webpage -> list.contains(webpage.getPageId()))
                .forEach(webpage -> {
            List<Goal> goals = GoalManager.getAllGoalsFromDBForCounter(session, webpage);
                    System.out.println(goals.size());
            goals.forEach(goal -> requester.fetchAllGoals(webpage, goal));
        });*/

        session.close();
    }

}
