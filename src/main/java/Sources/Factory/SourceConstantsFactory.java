package Sources.Factory;

import models.sources.*;

import java.util.Map;

public class SourceConstantsFactory {

    public static Map<Class, String> dimensionsMap;
    static {
        dimensionsMap.put(AdvEngine.class, "ym:s:lastsignAdvEngine");
        dimensionsMap.put(ReferralSource.class, "ym:s:lastsignReferalSource");
        dimensionsMap.put(SearchEngine.class, "ym:s:lastsignAdvEngine");
        dimensionsMap.put(SearchPhrase.class, "ym:s:lastsignAdvEngine");
        dimensionsMap.put(SocialNetwork.class, "ym:s:lastsignAdvEngine");
        dimensionsMap.put(TrafficSource.class, "ym:s:lastsignAdvEngine");
    }


}
