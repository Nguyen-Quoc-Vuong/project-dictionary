package application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DictionaryAPI extends APIService {

    // use to parse Json data
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public DictionaryAPI() {
        super(null, "https://api.dictionaryapi.dev/api/v2/entries/en/", null );
    }
    public JsonNode getWords(String word) throws Exception {
        String apiUrl = endpointUrl + word;
        StringBuilder result = new StringBuilder();

        HttpURLConnection con = createConnection(apiUrl);

        // read response
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            // read each line and add to StringBuilder
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }

        //convert StringBuilder into JsonNode
        return objectMapper.readTree(result.toString());
    }

    @Override
    public String makeRequestAPI(String word) {
        try {
            JsonNode response = getWords(word);
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error" + e.getMessage();
        }
    }
}
