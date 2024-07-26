package application.controller;

import application.model.BKTree;
import application.model.EnhancedWord;
import application.model.Word;
import application.service.DictionaryAPI;
import application.service.Pronounciation;
import application.util.DbConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
public class SearchController extends BaseController {
    @FXML
    private TableView<EnhancedWord> wordView;
    @FXML
    private TableColumn<EnhancedWord, String> wordColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button speakButton;

    @FXML
    private WebView webViewWord;

    @FXML
    private ToggleButton optionData;

    private boolean useAPI = false;
    private DictionaryAPI dictionaryAPI = new DictionaryAPI();

    private final ObservableList<EnhancedWord> allWords = FXCollections.observableArrayList();
    private final Pronounciation tts = new Pronounciation();
    private BKTree bkTree;

    // debounce to limit rate filter operation
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final long DELAY = 500;
    private static boolean isDataLoaded = false;
    @FXML
    private void initialize() {
        fetchDataFromDB();
        onSpeakButtonClick();
        setupWordView();
        setupSearchField();
        setupBKTree();
        System.out.println("Initialize");
    }

    private void setupWordView() {
        wordView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showWordDetails(newValue)
        );
    }

    public void refreshData() {
        fetchDataFromDB();
        System.out.println("RefreshData");
    }
    private void setupBKTree() {
        bkTree = new BKTree("");
        for (EnhancedWord word : allWords) {
            bkTree.insert(word.getWord_target());
        }
    }
    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            executor.schedule(() -> {
                Platform.runLater(() -> filterWords(newValue.trim()));
            }, DELAY, TimeUnit.MILLISECONDS);
        });
    }
    private void fetchDataFromDB() {
        new Thread(() -> {
            List<EnhancedWord> fetchedWords = new ArrayList<>();
            try (Connection connectDB = DbConnection.getInstance().getConnection();
                 Statement statement = connectDB.createStatement();
                 ResultSet queryResult = statement.executeQuery("SELECT word, pronounce, description FROM av")) {

                while (queryResult.next()) {
                    String word = queryResult.getString("word");
                    String definition = queryResult.getString("description");
                    String pronounceApi = queryResult.getString("pronounce");
                    fetchedWords.add(new EnhancedWord(word, definition, pronounceApi));
                }

                Platform.runLater(() -> {
                    wordColumn.setCellValueFactory(new PropertyValueFactory<>("word_target"));
                    allWords.clear();
                    allWords.addAll(fetchedWords);
                    wordView.setItems(allWords);
                    setupBKTree();
                });
            } catch (SQLException e) {
                Platform.runLater(() -> {
                    System.out.println("Error in fetching data from DB: " + e.getMessage());
                });
            }
        }).start();

    }

    @FXML
    private void handleOptionDataToggle() {
        useAPI = optionData.isSelected();
        if (!useAPI) {
            fetchDataFromDB();
        }
    }

    private void showWordDetails(EnhancedWord word) {
        if (word != null) {
            if (useAPI) {
                fetchWordDetailsFromAPI(word.getWord_target());
            } else {
                String wordDetails = generateHtmlForWord(word);
                webViewWord.getEngine().loadContent(wordDetails);
            }
        }
    }

    private void fetchWordDetailsFromAPI(String word) {
        new Thread(() -> {
            try {
                String wordDataJson = dictionaryAPI.makeRequestAPI(word);
                String wordDetailsHtml = generateHtmlFromAPI(wordDataJson);

                Platform.runLater(() -> {
                    webViewWord.getEngine().loadContent(wordDetailsHtml);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    webViewWord.getEngine().loadContent("Error loading word from api");
                });
            }
        }).start();
    }

    private String generateHtmlFromAPI(String wordDataJson) {
        if (wordDataJson.isEmpty()) {
            return "<p>No data available</p>";
        }
        StringBuilder html = new StringBuilder("<html><head><style>");
        html.append("body { font-family: 'Georgia', serif; background-color: #f4f4f4; color: #333; }");
        html.append("h2 { color: #4a86e8; }");
        html.append("h3 { color: #6a5acd; }");
        html.append("p { font-size: 16px; }");
        html.append(".example { font-style: italic;color: #008000; }");
        html.append("</style></head><body>");


        try {
            JsonNode wordData = new ObjectMapper().readTree(wordDataJson);

            for (JsonNode wordNode : wordData) {
                String word = wordNode.path("word").asText("N/A");
                String phonetic = wordNode.path("phonetic").asText("N/A");
                html.append("<h2>").append(word).append(" [").append(phonetic).append("]</h2>");
                JsonNode meaningsNode = wordNode.path("meanings");
                for (JsonNode meaning : meaningsNode) {
                    String partOfSpeech = meaning.path("partOfSpeech").asText("N/A");
                    html.append("<h3>").append(partOfSpeech).append("</h3>");
                    JsonNode definitionsNode = meaning.path("definitions");
                    for (JsonNode definitionNode : definitionsNode) {
                        String definition = definitionNode.path("definition").asText("N/A");
                        html.append("<p><b>Definition:</b> ").append(definition).append("</p>");
                        String example = definitionNode.path("example").asText("");
                        if (!example.isEmpty()) {
                            html.append("<p class='example'><i>Example:</i> ").append(example).append("</p>");
                        }
                    }
                }
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        html.append("</body></html>");
        return html.toString();
    }

    private String generateHtmlForWord(EnhancedWord word) {
        String wordName = word.getWord_target();
        String css = "<style>" +
                "  body { font-family: Arial, sans-serif; margin: 10px; }" +
                "  h1 { color: navy; margin-bottom: 0; }" +
                "  p { margin-top: 5px; font-size: 14px; }" +
                "  .word-container { border: 1px solid #ddd; padding: 15px; border-radius: 8px; background-color: #f9f9f9; }" +
                "</style>";

        String html = "<html><head>" + css + "</head><body>" +
                "<div class='word-container'>" +
                "<h1>" + word.getWord_target() + "</h1>" +
                "<p>" + word.getPronounce() + "</p>" +
                "<p>" + word.getWord_explain() + "</p>" +
                "</div>" +
                "</body></html>";
        return html;
    }

    private void filterWords(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            Platform.runLater(() -> wordView.setItems(allWords));
            return;
        }

        List<String> results = new ArrayList<>();
        bkTree.search(searchText, 1, results);

        ObservableList<EnhancedWord> filteredWords = allWords.stream()
                .filter(word -> results.contains(word.getWord_target()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        wordView.setItems(filteredWords);
    }

    @FXML
    private void onSpeakButtonClick() {
        speakButton.setOnAction(event -> {
            Word selectedWord = wordView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                String wordToSpeak = selectedWord.getWord_target();
                tts.synText(wordToSpeak);
//                SpeakTTS.synthesizeText(wordToSpeak);
            }
        });
    }
}
