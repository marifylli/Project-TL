package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ThesisFormScreen {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextArea prerequisitesArea;
    @FXML private TextField ectsField;
    @FXML private TextField maxCandidatesField;
    @FXML private TextArea requiredSkillsArea;
    @FXML private Label errorLabel;

    private final ManageThesisClass controller = new ManageThesisClass();
    private int professorId = 1; // TODO: παίρνει από το login

    @FXML
    private void onSelectCalendar() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/fxml/meeting-calendar-view.fxml"));
            javafx.scene.Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ημερολόγιο Συναντήσεων");
            stage.setScene(new javafx.scene.Scene(root, 600, 500));
            stage.show();
        } catch (Exception e) {
            errorLabel.setText("Σφάλμα ανοίγματος ημερολογίου!");
        }
    }

    @FXML
    private void onPublishThesis() {
        errorLabel.setText("");

        if (!validateForm()) return;

        int ects;
        int maxCandidates;
        try {
            ects = Integer.parseInt(ectsField.getText().trim());
            maxCandidates = Integer.parseInt(maxCandidatesField.getText().trim());
        } catch (NumberFormatException e) {
            errorLabel.setText("Τα ECTS και ο αριθμός υποψηφίων πρέπει να είναι αριθμοί!");
            return;
        }

        boolean success = controller.publishThesis(
                professorId,
                titleField.getText().trim(),
                descriptionArea.getText().trim(),
                prerequisitesArea.getText().trim(),
                ects,
                maxCandidates,
                requiredSkillsArea.getText().trim()
        );

        if (success) {
            showSuccess();
        } else {
            errorLabel.setText("Σφάλμα δημοσίευσης! Παρακαλώ προσπαθήστε ξανά.");
        }
    }

    private boolean validateForm() {
        if (titleField.getText().isBlank()) {
            errorLabel.setText("Ο τίτλος είναι υποχρεωτικός!");
            return false;
        }
        if (descriptionArea.getText().isBlank()) {
            errorLabel.setText("Η περιγραφή είναι υποχρεωτική!");
            return false;
        }
        if (prerequisitesArea.getText().isBlank()) {
            errorLabel.setText("Τα προαπαιτούμενα είναι υποχρεωτικά!");
            return false;
        }
        if (requiredSkillsArea.getText().isBlank()) {
            errorLabel.setText("Οι δεξιότητες είναι υποχρεωτικές!");
            return false;
        }
        if (ectsField.getText().isBlank()) {
            errorLabel.setText("Τα ECTS είναι υποχρεωτικά!");
            return false;
        }
        if (maxCandidatesField.getText().isBlank()) {
            errorLabel.setText("Ο αριθμός υποψηφίων είναι υποχρεωτικός!");
            return false;
        }
        return true;
    }

    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Επιτυχής Δημοσίευση");
        alert.setHeaderText(null);
        alert.setContentText("Η διπλωματική δημοσιεύτηκε επιτυχώς!");
        alert.showAndWait();
        clearForm();
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        prerequisitesArea.clear();
        ectsField.clear();
        maxCandidatesField.clear();
        requiredSkillsArea.clear();
        errorLabel.setText("");
    }
}