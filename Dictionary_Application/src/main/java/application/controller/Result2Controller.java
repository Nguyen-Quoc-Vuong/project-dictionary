package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class Result2Controller extends BaseController{

    @FXML
    private Label point;

    @FXML
    public Button againButton;

    public void initialize(int score) {
        point.setText(String.valueOf(score));
    }

    @FXML
    private void onPlayAgainClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/game2.fxml"));
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
