package application.controller;

import application.database.DataProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class SettingController extends BaseController {
    @FXML
    private TextField wordField, meaningField, updateWordField, updateMeaningField, deleteWordField;

    @FXML
    private Button addWordButton, updateWordButton, deleteWordButton, saveSettingButton, cancelButton;

    private final DataProcess dataProcess = new DataProcess();
    private SearchController searchController;

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    @FXML
    private void handleAddWord(ActionEvent event) throws SQLException {
        String word = wordField.getText();
        String meaning = meaningField.getText();

        if (!word.isEmpty() && !meaning.isEmpty()) {
            if (!dataProcess.wordExistsInDatabase(word)) {
                try {
                    dataProcess.addWordToDatabase(word, meaning);
                    wordField.clear();
                    meaningField.clear();
                    showAlert(Alert.AlertType.INFORMATION,"Success", "Word added successfully");
                    searchController.refreshData();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR,"Error", "Failed to add word.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Word already exists");
            }

        } else {
            showAlert(Alert.AlertType.WARNING,"Error", "Word and meaning cannot be empty");
        }
    }

    @FXML
    private void handleDeleteWord(ActionEvent event) {
        String word = deleteWordField.getText();
        if (!word.isEmpty()) {
            try {
                if (dataProcess.wordExistsInDatabase(word)) {
                    dataProcess.deleteWordFromDatabase(word);
                    deleteWordField.clear(); // Clear the field after deletion
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Word deleted successfully");
                    searchController.refreshData();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Word does not exist in database");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error while deleting.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Error", "Word field cannot be empty");
        }
    }

    @FXML
    private void handleUpdateWord(ActionEvent event) throws SQLException {
        String word = updateWordField.getText();
        String newMeaning = updateMeaningField.getText();
        if (!word.isEmpty() && !newMeaning.isEmpty()) {
            if (dataProcess.wordExistsInDatabase(word)) {
                try {
                    dataProcess.updateWordInDatabase(word, newMeaning);
                    updateWordField.clear();
                    updateMeaningField.clear();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Word updated successfully");
                    searchController.refreshData();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR,"Error", "Error while updating word");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Word does not exist");
            }

        } else {
            showAlert(Alert.AlertType.WARNING, "Error", "Please fill both word and new meaning");
        }
    }

    @FXML
    private void handleSaveSetting() {
        try {
            System.out.println("save setting");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Save error", "Failed to save setting");
        }
    }
}