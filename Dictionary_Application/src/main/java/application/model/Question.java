package application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class Question {
    private final StringProperty questionText = new SimpleStringProperty();
    private final List<String> dummyOptions;
    private final StringProperty correctAnswer = new SimpleStringProperty();

    public Question(String questionText, List<String> dummyOptions, String correctAnswer) {
        this.questionText.set(questionText);
        this.dummyOptions = dummyOptions;
        this.correctAnswer.set(correctAnswer);
    }

    public StringProperty questionTextProperty() {
        return questionText;
    }

    public String getQuestionText() {
        return questionText.get();
    }

    public List<String> getDummyOptions() {
        return dummyOptions;
    }

    public StringProperty correctAnswerProperty() {
        return correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer.get();
    }
}

