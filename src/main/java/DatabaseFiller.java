import Interfaces.Fetcher;
import Interfaces.JsonParser;
import Interfaces.SessionManager;
import Sources.AdvEngine.AdvEngineManager;
import Sources.Factory.SourceManagerFactory;
import Sources.ReferralSource.ReferralSourceManager;
import Sources.SearchEngine.SearchEngineManager;
import Sources.SearchPhrase.SearchPhraseFiller;
import Sources.SearchPhrase.SearchPhraseManager;
import Sources.SocialNetwork.SocialNetworkManager;

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

        //Search engine filler

        SearchEngineManager seManager = sourceFactory.getSearchEngineManager();
        //SearchEngineFiller seFiller = new SearchEngineFiller(seManager, date, fetcher, jsonParser);
        //seFiller.fillDatabase();

        //Social network filler

        SocialNetworkManager snManager = sourceFactory.getSocialNetworkManager();
        //SocialNetworkFiller snFiller = new SocialNetworkFiller(snManager, date, fetcher, jsonParser);
        //snFiller.fillDatabase();

        //Referral source filler

        ReferralSourceManager rsManager = sourceFactory.getReferralSourceManager();
        //ReferralSourceFiller rsFiller = new ReferralSourceFiller(rsManager, date, fetcher, jsonParser);
        //rsFiller.fillDatabase();

        //AdvEngine filler

        AdvEngineManager aeManager = sourceFactory.getAdvEngineManager();
        //AdvEngineFiller aeFiller = new AdvEngineFiller(aeManager, date, fetcher, jsonParser);
        //aeFiller.fillDatabase();

        //Search Phrase filler

        SearchPhraseManager spManager = sourceFactory.getSearchPhraseManager();
        //SearchPhraseFiller spFiller = new SearchPhraseFiller(spManager, date, fetcher, jsonParser);
        //spFiller.fillDatabase();

    }

}
