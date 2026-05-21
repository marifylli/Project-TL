package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.Thesis;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

public class ThesisFormScreen {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextArea prerequisitesArea;
    @FXML private TextField ectsField;
    @FXML private TextField maxCandidatesField;
    @FXML private TextArea requiredSkillsArea;
    @FXML private Label errorLabel;

    private final ManageThesisClass controller = new ManageThesisClass();
    private int professorId = 1;

    @FXML
    private void onSelectCalendar() {
        if (!validateForm()) return;

        try {
            int ects = Integer.parseInt(ectsField.getText().trim());
            int maxCandidates = Integer.parseInt(maxCandidatesField.getText().trim());

            Thesis temporaryThesis = new Thesis(
                    professorId,
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    prerequisitesArea.getText().trim(),
                    ects,
                    maxCandidates,
                    requiredSkillsArea.getText().trim()
            );

            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/fxml/Professor/meeting-calendar-view.fxml"));
            javafx.scene.Parent root = loader.load();

            MeetingCalendarScreen calendarScreen = loader.getController();
            calendarScreen.setThesisContext(temporaryThesis, (Stage) titleField.getScene().getWindow());

            Stage stage = new Stage();
            stage.setTitle("Ημερολόγιο Συναντήσεων");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (NumberFormatException e) {
            showErrorWindow();
        } catch (Exception e) {
            showErrorWindow();
        }
    }

    private boolean validateForm() {
        if (titleField.getText().isBlank() || descriptionArea.getText().isBlank() ||
                prerequisitesArea.getText().isBlank() || requiredSkillsArea.getText().isBlank() ||
                ectsField.getText().isBlank() || maxCandidatesField.getText().isBlank()) {

            showErrorWindow();
            return false;
        }
        return true;
    }

    private void showErrorWindow() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/fxml/common/error-window-view.fxml"));
            javafx.scene.Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Οθόνη Σφάλματος");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            errorLabel.setText("Σφάλμα κατά τη συμπλήρωση των πεδίων.");
        }
    }
}