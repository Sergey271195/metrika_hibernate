package Interfaces;

import org.json.simple.JSONObject;

import java.util.Map;

public interface JsonParser {
    Map<String, Object> parse(String response);
}
