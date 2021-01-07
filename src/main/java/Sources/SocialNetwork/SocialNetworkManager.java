package Sources.SocialNetwork;

import Interfaces.SessionManager;
import Sources.Abstract.SourceManager;
import models.sources.SocialNetwork;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SocialNetworkManager extends SourceManager<SocialNetwork> {

    public SocialNetworkManager(SessionManager sm) {
        super(sm, SocialNetwork.class);
    }

    @Override
    public void updateSourcesMap() {
        List<SocialNetwork> sourceList = this.fetchSourcesList();
        this.sources = sourceList.stream().collect(Collectors.toMap(SocialNetwork::getId, Function.identity()));
    }

    @Override
    public void createNewSourceInstance(Map<String, List> responseData) {
        Map<String, String> dimensions = (Map) responseData.get("dimensions").get(0);
        if (!sourceExists(dimensions.get("id"))) {
            SocialNetwork newNetwork = createNewSocialNetwork(dimensions.get("id"), dimensions.get("name"));
            persistNewNetwork(newNetwork);
            updateSourcesMap();
        }
    }

    private void persistNewNetwork(SocialNetwork newNetwork) {
        Session session = sm.startSession();
        Transaction tx = session.beginTransaction();
        session.persist(newNetwork);
        tx.commit();
        session.close();
    }

    private SocialNetwork createNewSocialNetwork(String id, String name) {
        SocialNetwork newNetwork = new SocialNetwork();
        newNetwork.setId(id);
        newNetwork.setName(name);
        return newNetwork;
    }
}
