package Sources.Factory;

import Interfaces.SessionManager;
import Sources.Abstract.SourceManager;
import Sources.AdvEngine.AdvEngineManager;
import Sources.ReferralSource.ReferralSourceManager;
import Sources.SearchEngine.SearchEngineManager;
import Sources.SearchPhrase.SearchPhraseManager;
import Sources.SocialNetwork.SocialNetworkManager;
import models.SocialNetwork;

public class SourceManagerFactory {

    private final AdvEngineManager advEngineManager;
    private final ReferralSourceManager referralSourceManager;
    private final SearchEngineManager searchEngineManager;
    private final SearchPhraseManager searchPhraseManager;
    private final SocialNetworkManager socialNetworkManager;

    public SourceManagerFactory(SessionManager sessionManager) {
        advEngineManager = new AdvEngineManager(sessionManager);
        referralSourceManager = new ReferralSourceManager(sessionManager);
        searchEngineManager = new SearchEngineManager(sessionManager);
        searchPhraseManager = new SearchPhraseManager(sessionManager);
        socialNetworkManager = new SocialNetworkManager(sessionManager);
    }

    public enum SourceManagers {
        ADV_ENGINE, REFERRAL_SOURCE, SEARCH_ENGINE, SEARCH_PHRASE, SOCIAL_NETWORK
    }

    public SourceManager getManager(SourceManagers manager) {
        switch (manager) {
            case ADV_ENGINE:
                return advEngineManager;
            case REFERRAL_SOURCE:
                return referralSourceManager;
            case SEARCH_ENGINE:
                return searchEngineManager;
            case SEARCH_PHRASE:
                return searchPhraseManager;
            case SOCIAL_NETWORK:
                return socialNetworkManager;
            default:
                return null;
        }
    }

    public AdvEngineManager getAdvEngineManager() {
        return advEngineManager;
    }

    public SearchEngineManager getSearchEngineManager() {
        return searchEngineManager;
    }

    public ReferralSourceManager getReferralSourceManager() {
        return referralSourceManager;
    }

    public SearchPhraseManager getSearchPhraseManager() {
        return searchPhraseManager;
    }

    public SocialNetworkManager getSocialNetworkManager() {
        return socialNetworkManager;
    }

}
