import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Sources.AdvEngine.AdvEngineManager;
import Sources.AdvEngine.AdvEngineViewsFiller;
import Sources.Factory.SourceManagerFactory;
import Sources.ReferralSource.ReferralSourceManager;
import Sources.ReferralSource.ReferralSourceViewsFiller;
import Sources.SearchEngine.SearchEngineManager;
import Sources.SearchEngine.SearchEngineViewsFiller;
import Sources.SearchPhrase.SearchPhraseManager;
import Sources.SearchPhrase.SearchPhraseViewsFiller;
import Sources.SocialNetwork.SocialNetworkManager;
import Sources.SocialNetwork.SocialNetworkViewsFiller;
import Sources.TrafficSource.TrafficSourceGoalsFiller;
import Sources.TrafficSource.TrafficSourceManager;
import Sources.TrafficSource.TrafficSourceViewsFiller;

import java.time.LocalDate;

public class DatabaseFiller {

    private final Fetcher fetcher;
    private final JsonParser jsonParser;
    private final SourceManagerFactory sourceFactory;

    public DatabaseFiller(Fetcher fetcher, JsonParser jsonParser, SourceManagerFactory sourceFactory) {
        this.fetcher = fetcher;
        this.jsonParser = jsonParser;
        this.sourceFactory = sourceFactory;
    }

    public void fillUntilDate(LocalDate date) {

        //Traffic source filler

        TrafficSourceManager tsManager = sourceFactory.getTrafficSourceManager();
        //TrafficSourceGoalsFiller tsFiller = new TrafficSourceGoalsFiller(tsManager, date, fetcher, jsonParser);
        //tsFiller.fillDatabase();

        //TrafficSourceViewsFiller tsvFiller = new TrafficSourceViewsFiller(tsManager, date, fetcher, jsonParser);
        //tsvFiller.fillDatabase();

        //Search engine filler

        SearchEngineManager seManager = sourceFactory.getSearchEngineManager();
        //SearchEngineFiller seFiller = new SearchEngineFiller(seManager, date, fetcher, jsonParser);
        //seFiller.fillDatabase();

        //SearchEngineViewsFiller sevFiller = new SearchEngineViewsFiller(seManager, date, fetcher, jsonParser);
        //sevFiller.fillDatabase();

        //Social network filler

        SocialNetworkManager snManager = sourceFactory.getSocialNetworkManager();
        //SocialNetworkFiller snFiller = new SocialNetworkFiller(snManager, date, fetcher, jsonParser);
        //snFiller.fillDatabase();

        //SocialNetworkViewsFiller snvFiller = new SocialNetworkViewsFiller(snManager, date, fetcher, jsonParser);
        //snvFiller.fillDatabase();

        //Referral source filler

        ReferralSourceManager rsManager = sourceFactory.getReferralSourceManager();
        //ReferralSourceFiller rsFiller = new ReferralSourceFiller(rsManager, date, fetcher, jsonParser);
        //rsFiller.fillDatabase();

        ReferralSourceViewsFiller rsvFiller = new ReferralSourceViewsFiller(rsManager, date, fetcher, jsonParser);
        rsvFiller.fillDatabase();

        //AdvEngine filler

        AdvEngineManager aeManager = sourceFactory.getAdvEngineManager();
        //AdvEngineFiller aeFiller = new AdvEngineFiller(aeManager, date, fetcher, jsonParser);
        //aeFiller.fillDatabase();

        //AdvEngineViewsFiller aevFiller = new AdvEngineViewsFiller(aeManager, date, fetcher, jsonParser);
        //aevFiller.fillDatabase();

        //Search Phrase filler

        SearchPhraseManager spManager = sourceFactory.getSearchPhraseManager();
        //SearchPhraseFiller spFiller = new SearchPhraseFiller(spManager, date, fetcher, jsonParser);
        //spFiller.fillDatabase();

        //SearchPhraseViewsFiller spvFiller = new SearchPhraseViewsFiller(spManager, date, fetcher, jsonParser);
        //spvFiller.fillDatabase();

    }

}
