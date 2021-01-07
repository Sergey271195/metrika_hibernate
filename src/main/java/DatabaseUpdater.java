import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.AdvEngine.AdvEngineManager;
import Sources.AdvEngine.AdvEngineGoalsUpdater;
import Sources.AdvEngine.AdvEngineViewsUpdater;
import Sources.Factory.SourceManagerFactory;
import Sources.ReferralSource.ReferralSourceManager;
import Sources.ReferralSource.ReferralSourceGoalsUpdater;
import Sources.ReferralSource.ReferralSourceViewsUpdater;
import Sources.SearchEngine.SearchEngineManager;
import Sources.SearchEngine.SearchEngineGoalsUpdater;
import Sources.SearchEngine.SearchEngineViewsUpdater;
import Sources.SearchPhrase.SearchPhraseManager;
import Sources.SearchPhrase.SearchPhraseGoalsUpdater;
import Sources.SearchPhrase.SearchPhraseViewsUpdater;
import Sources.SocialNetwork.SocialNetworkManager;
import Sources.SocialNetwork.SocialNetworkGoalsUpdater;
import Sources.SocialNetwork.SocialNetworkViewsUpdater;
import Sources.TrafficSource.TrafficSourceManager;
import Sources.TrafficSource.TrafficSourceGoalsUpdater;
import Sources.TrafficSource.TrafficSourceViewsUpdater;

public class DatabaseUpdater {

    private final Fetcher fetcher;
    private final JsonParser jsonParser;
    private final SourceManagerFactory sourceFactory;

    public DatabaseUpdater(Fetcher fetcher, JsonParser jsonParser, SourceManagerFactory sourceFactory) {
        this.fetcher = fetcher;
        this.jsonParser = jsonParser;
        this.sourceFactory = sourceFactory;
    }

    public void updateDatabase() {


        //Traffic source update
        TrafficSourceManager tsManager = sourceFactory.getTrafficSourceManager();

        TrafficSourceGoalsUpdater tsUpdater = new TrafficSourceGoalsUpdater(tsManager, fetcher, jsonParser);
        tsUpdater.updateDatabase();

        TrafficSourceViewsUpdater tsvUpdater = new TrafficSourceViewsUpdater(tsManager, fetcher, jsonParser);
        tsvUpdater.updateDatabase();

        //Search engine update
        SearchEngineManager seManager = sourceFactory.getSearchEngineManager();

        SearchEngineGoalsUpdater seUpdater = new SearchEngineGoalsUpdater(seManager, fetcher, jsonParser);
        seUpdater.updateDatabase();

        SearchEngineViewsUpdater sevUpdater = new SearchEngineViewsUpdater(seManager, fetcher, jsonParser);
        sevUpdater.updateDatabase();

        //Social network update
        SocialNetworkManager snManager = sourceFactory.getSocialNetworkManager();

        SocialNetworkGoalsUpdater snUpdater = new SocialNetworkGoalsUpdater(snManager, fetcher, jsonParser);
        snUpdater.updateDatabase();

        SocialNetworkViewsUpdater snvUpdater = new SocialNetworkViewsUpdater(snManager, fetcher, jsonParser);
        snvUpdater.updateDatabase();

        //Referral source update
        ReferralSourceManager rsManager = sourceFactory.getReferralSourceManager();

        ReferralSourceGoalsUpdater rsUpdater = new ReferralSourceGoalsUpdater(rsManager, fetcher, jsonParser);
        rsUpdater.updateDatabase();

        ReferralSourceViewsUpdater rsvUpdater = new ReferralSourceViewsUpdater(rsManager, fetcher, jsonParser);
        rsvUpdater.updateDatabase();

        //AdvEngine update
        AdvEngineManager aeManager = sourceFactory.getAdvEngineManager();

        AdvEngineGoalsUpdater aeUpdater = new AdvEngineGoalsUpdater(aeManager, fetcher, jsonParser);
        aeUpdater.updateDatabase();

        AdvEngineViewsUpdater aevUpdater = new AdvEngineViewsUpdater(aeManager, fetcher, jsonParser);
        aevUpdater.updateDatabase();

        //Search Phrase update
        SearchPhraseManager spManager = sourceFactory.getSearchPhraseManager();

        SearchPhraseGoalsUpdater spUpdater = new SearchPhraseGoalsUpdater(spManager, fetcher, jsonParser);
        spUpdater.updateDatabase();

        SearchPhraseViewsUpdater spvUpdater = new SearchPhraseViewsUpdater(spManager, fetcher, jsonParser);
        spvUpdater.updateDatabase();
    }

}
