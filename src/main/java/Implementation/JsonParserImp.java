package Implementation;

import Interfaces.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonParserImp implements JsonParser {

    public Object parseRecursive(Object obj) {
        Map<String, Object> parsedResult = new HashMap<>();
        if (obj.getClass() == JSONObject.class) {

            JSONObject response = (JSONObject) obj;
            for (Object key: response.keySet()) {
                Object value = response.get(key);
                //System.out.println("" + key + response.get(key) + value.getClass());
                if (value.getClass() == JSONArray.class) {
                    JSONArray array = (JSONArray) value;
                    Object newArr = array.stream()
                            .map(innerKey -> parseRecursive(innerKey))
                            .collect(Collectors.toList());
                    parsedResult.put((String) key, newArr);
                } else if (value.getClass() == JSONObject.class) {
                    Map<String, Object> innerMap = (Map<String, Object>) parseRecursive(value);
                    parsedResult.put((String) key, innerMap);
                } else {
                    parsedResult.put((String) key, value);
                }
            }
        } else {
            return obj;
        }
        return parsedResult;
    }

    @Override
    public Map<String,Object> parse(String response) {
        Map<String, Object> parsedResponse = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response);
            parsedResponse = (Map<String, Object>) parseRecursive(jsonObject);
        } catch (ParseException e) {
            System.out.println("Exception while parsing " + e);
        }
        return parsedResponse;
    }
}
