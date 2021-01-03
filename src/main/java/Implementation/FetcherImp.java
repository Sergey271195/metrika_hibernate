package Implementation;

import Interfaces.Fetcher;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FetcherImp implements Fetcher {

    private static String METRIC_TOKEN = System.getenv("METRIC_TOKEN");

    @Override
    public String fetch(String url) {
        try {
            URI requestUrl = new URI(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(requestUrl)
                    .headers("Authorization", "OAuth " + METRIC_TOKEN)
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                return response.body();
            } throw new java.io.IOException(
                    "Exception while making request. STATUS_CODE = " + statusCode + ". For url = " + url);
        } catch (java.net.URISyntaxException|java.io.IOException|java.lang.InterruptedException e) {
            System.out.println(e);
            return "{}";
        }
    }
}
