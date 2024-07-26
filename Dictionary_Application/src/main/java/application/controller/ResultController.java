package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class ResultController extends BaseController{

    @FXML
    private Label comment, marks;

    @FXML
    private ProgressIndicator correctProgress, wrongProgress;

    @FXML
    public Button againButton;

    public void initialize(int score, int totalQuestions) {
        // Tính toán các giá trị cần thiết
        double percentageCorrect = (double) score / totalQuestions;
        double percentageWrong = 1 - percentageCorrect;

        // Hiển thị số điểm
        marks.setText(score + "/" + totalQuestions);

        // Hiển thị % đúng và % sai
        correctProgress.setProgress(percentageCorrect);
        wrongProgress.setProgress(percentageWrong);

        // Hiển thị comment dựa trên điểm số
        if (percentageCorrect >= 0.75) {
            comment.setText("Excellent");
        } else {
            comment.setText("Try Hard");
        }
    }

    @FXML
    private void onPlayAgainClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/game.fxml"));
            Parent resultView = (Parent) loader.load();
            Stage primaryStage = (Stage) againButton.getScene().getWindow();
            primaryStage.setScene(new Scene(resultView));
            primaryStage.setTitle("Game");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}