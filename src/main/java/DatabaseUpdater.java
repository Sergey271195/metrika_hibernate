import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.AdvEngine.AdvEngineManager;
import Sources.AdvEngine.AdvEngineUpdater;
import Sources.Factory.SourceManagerFactory;
import Sources.ReferralSource.ReferralSourceManager;
import Sources.ReferralSource.ReferralSourceUpdater;
import Sources.SearchEngine.SearchEngineManager;
import Sources.SearchEngine.SearchEngineUpdater;
import Sources.SearchPhrase.SearchPhraseManager;
import Sources.SearchPhrase.SearchPhraseUpdater;
import Sources.SocialNetwork.SocialNetworkManager;
import Sources.SocialNetwork.SocialNetworkUpdater;
import Sources.TrafficSource.TrafficSourceManager;
import Sources.TrafficSource.TrafficSourceUpdater;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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
        TrafficSourceUpdater tsUpdater = new TrafficSourceUpdater(tsManager, fetcher, jsonParser);
        tsUpdater.updateDatabase();

        //Search engine update
        SearchEngineManager seManager = sourceFactory.getSearchEngineManager();
        SearchEngineUpdater seUpdater = new SearchEngineUpdater(seManager, fetcher, jsonParser);
        seUpdater.updateDatabase();

        //Social network update
        SocialNetworkManager snManager = sourceFactory.getSocialNetworkManager();
        SocialNetworkUpdater snUpdater = new SocialNetworkUpdater(snManager, fetcher, jsonParser);
        snUpdater.updateDatabase();

        //Referral source update
        ReferralSourceManager rsManager = sourceFactory.getReferralSourceManager();
        ReferralSourceUpdater rsUpdater = new ReferralSourceUpdater(rsManager, fetcher, jsonParser);
        rsUpdater.updateDatabase();

        //AdvEngine update
        AdvEngineManager aeManager = sourceFactory.getAdvEngineManager();
        AdvEngineUpdater aeUpdater = new AdvEngineUpdater(aeManager, fetcher, jsonParser);
        aeUpdater.updateDatabase();

        //Search Phrase update
        SearchPhraseManager spManager = sourceFactory.getSearchPhraseManager();
        SearchPhraseUpdater spUpdater = new SearchPhraseUpdater(spManager, fetcher, jsonParser);
        spUpdater.updateDatabase();
    }

}
