package application.service;

import java.net.HttpURLConnection;
import java.net.URL;

public abstract class APIService {
    protected String apiKey;
    protected String endpointUrl;
    protected String region;
    public APIService(String apiKey, String endpointUrl, String region) {
        this.apiKey = apiKey;
        this.endpointUrl = endpointUrl;
        this.region = region;
    }

    protected HttpURLConnection createConnection(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    public abstract String makeRequestAPI(String inputData);
}
