package application.controller;

import application.model.Question;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GameController {
    @FXML
    private Label rightAnswer, checkAnswer, questionLabel;

    @FXML
    private Button answer1, answer2, answer3, answer4;

    private List<Question> questions;
    private int currentQuestionIndex;

    private Timeline answerStatusTimeline;

    private int score = 0;
    private int maxWrongAttempts = 3;
    private int wrongAttempts = 0;

    private int numberOfDummyOptions = 3;

    public void initialize() {
        // Load questions from file
        loadQuestionsFromFile("/application/dictionary/dictionary.txt");

        // Shuffle questions
        Collections.shuffle(questions);

        answerStatusTimeline = new Timeline();

        // Thiết lập sự kiện kết thúc cho Timeline
        answerStatusTimeline.setOnFinished(event -> {
            checkAnswer.setText(""); // Ẩn đi nội dung khi kết thúc
        });

        // Display the first question
        showNextQuestion();
    }

    private void loadQuestionsFromFile(String filePath) {
        questions = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(filePath);
             Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\t");
                if (parts.length == 2) {
                    String englishWord = parts[0].trim();
                    String correctAnswer = parts[1].trim();

                    List<String> dummyOptions = generateDummyOptions(new Question(englishWord, null, correctAnswer));

                    Question question = new Question(englishWord, dummyOptions, correctAnswer);
                    questions.add(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Add error handling
        }
    }

    private List<String> generateDummyOptions(Question currentQuestion) {
        List<String> dummyOptions = new ArrayList<>();

        String correctAnswer = currentQuestion.getCorrectAnswer();

        List<Question> newQuestions = new ArrayList<>(questions);
        Collections.shuffle(newQuestions);

        int count = 0;

        for (Question q : newQuestions) {
            if (!q.equals(currentQuestion)) {
                dummyOptions.add(q.getCorrectAnswer());
                count++;

                if (count >= numberOfDummyOptions) {
                    break;
                }
            }
        }


        dummyOptions.add(correctAnswer);

        return dummyOptions;
    }



    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size() && wrongAttempts < maxWrongAttempts) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestionText());

            List<Button> answerButtons = List.of(answer1, answer2, answer3, answer4);
            List<String> options = currentQuestion.getDummyOptions();

            if (!options.contains(currentQuestion.getCorrectAnswer())) {
                options.set(0, currentQuestion.getCorrectAnswer());
            }

            Collections.shuffle(options);

            for (int i = 0; i < answerButtons.size(); i++) {
                answerButtons.get(i).setText(options.get(i));
            }

            currentQuestionIndex++;
        } else {
            // End of the game
            questionLabel.setText("Game Over");
            answer1.setDisable(true);
            answer2.setDisable(true);
            answer3.setDisable(true);
            answer4.setDisable(true);

            // Display the player's score
            showResult(score, currentQuestionIndex);
        }
    }
    private void showResult(int score, int totalQuestions) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/result.fxml"));
            StackPane resultPane = loader.load();

            // Truy cập controller của ResultController
            application.controller.ResultController resultController = loader.getController();

            // Pass thông tin kết quả
            resultController.initialize(score, totalQuestions);

            // Hiển thị cửa sổ kết quả
            Stage resultStage = new Stage();
            Scene scene = new Scene(resultPane);
            resultStage.setScene(scene);
            resultStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý nếu có lỗi khi tạo cửa sổ kết quả
        }
    }

    private void checkAnswer(String selectedAnswer) {
        Question currentQuestion = questions.get(currentQuestionIndex - 1);
        String correctAnswer = currentQuestion.getCorrectAnswer();
        if (selectedAnswer.equals(correctAnswer)) {
            score++;  // Increase the score for correct answers
            checkAnswer.setText("Correct");
            checkAnswer.setStyle("-fx-text-fill: green;");
        } else {
            checkAnswer.setText("Wrong");
            checkAnswer.setStyle("-fx-text-fill: red;");
            rightAnswer.setText("Correct Answer: " + correctAnswer);
            rightAnswer.setStyle("-fx-text-fill: green;");
            wrongAttempts++;
        }

        answerStatusTimeline.getKeyFrames().clear();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), new KeyValue(checkAnswer.textProperty(), ""));
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(2), new KeyValue(rightAnswer.textProperty(), ""));
        answerStatusTimeline.getKeyFrames().add(keyFrame);
        answerStatusTimeline.getKeyFrames().add(keyFrame1);
        answerStatusTimeline.play();
    }

    @FXML
    private void handleAnswerButtonClick(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String selectedAnswer = clickedButton.getText();
        checkAnswer(selectedAnswer);
        showNextQuestion();
    }

    @FXML
    private void answer1Clicked() {
        handleAnswerButtonClick(new javafx.event.ActionEvent(answer1, null));
    }

    @FXML
    private void answer2Clicked() {
        handleAnswerButtonClick(new javafx.event.ActionEvent(answer2, null));
    }

    @FXML
    private void answer3Clicked() {
        handleAnswerButtonClick(new javafx.event.ActionEvent(answer3, null));
    }

    @FXML
    private void answer4Clicked() {
        handleAnswerButtonClick(new javafx.event.ActionEvent(answer4, null));
    }

}