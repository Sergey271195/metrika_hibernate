package Sources.ReferralSource;

import Interfaces.SessionManager;
import Sources.Abstract.SourceManager;
import models.sources.ReferralSource;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReferralSourceManager extends SourceManager<ReferralSource> {

    public ReferralSourceManager(SessionManager sm) {
        super(sm, ReferralSource.class);
    }

    @Override
    public void updateSourcesMap() {
        List<ReferralSource> sourceList = this.fetchSourcesList();
        this.sources = sourceList.stream().collect(Collectors.toMap(ReferralSource::getId, Function.identity()));
    }

    @Override
    public void createNewSourceInstance(Map<String, List> responseData) {
        Map<String, String> dimensions = (Map) responseData.get("dimensions").get(0);
        if (!sourceExists(dimensions.get("name"))) {
            ReferralSource newReferral = createNewReferralSource(dimensions.get("name"), dimensions.get("name"));
            persistNewEngine(newReferral);
            this.sources.put(newReferral.getId(), newReferral);
        }
    }

    public void createNewSourceInstanceForFiller(Map<String, List> responseData) {
        Map<String, String> dimensions = (Map) responseData.get("dimension");
        System.out.println(dimensions.get("name"));
        if (!sourceExists(dimensions.get("name"))) {
            ReferralSource newReferral = createNewReferralSource(dimensions.get("name"), dimensions.get("name"));
            persistNewEngine(newReferral);
            this.sources.put(newReferral.getId(), newReferral);
        }
    }

    private void persistNewEngine(ReferralSource newReferral) {
        Session session = sm.startSession();
        Transaction tx = session.beginTransaction();
        session.persist(newReferral);
        tx.commit();
        session.close();
    }

    private ReferralSource createNewReferralSource(String id, String name) {
        ReferralSource newReferral = new ReferralSource();
        newReferral.setId(id);
        newReferral.setName(name);
        return newReferral;
    }
}
