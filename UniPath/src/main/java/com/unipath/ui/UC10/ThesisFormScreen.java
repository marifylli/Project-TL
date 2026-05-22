package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.Thesis;
import com.unipath.ui.common.ErrorScreen;
import com.unipath.login.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    @FXML
    private void onSelectCalendar() {
        if (!validateForm()) return;

        try {
            int ects = Integer.parseInt(ectsField.getText().trim());
            int maxCandidates = Integer.parseInt(maxCandidatesField.getText().trim());

            String prereqs = prerequisitesArea.getText() == null ? "" : prerequisitesArea.getText().trim();
            String skills = requiredSkillsArea.getText() == null ? "" : requiredSkillsArea.getText().trim();

            int activeProfessorId = UserSession.getInstance().getUserId();

            Thesis temporaryThesis = new Thesis(
                    activeProfessorId,
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    prereqs,
                    ects,
                    maxCandidates,
                    skills
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/meeting-calendar-view.fxml"));
            Parent root = loader.load();

            MeetingCalendarScreen calendarScreen = loader.getController();

            if (calendarScreen == null) {
                showErrorWindow("Σφάλμα: Δεν βρέθηκε ο Controller του Ημερολογίου!\nΕλέγξτε το fx:controller στο meeting-calendar-view.fxml");
                return;
            }

            calendarScreen.setThesisContext(temporaryThesis, (Stage) titleField.getScene().getWindow());

            Stage stage = new Stage();
            stage.setTitle("Ημερολόγιο Συναντήσεων");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (NumberFormatException e) {
            showErrorWindow("Τα πεδία ECTS και Μέγιστος Αριθμός Υποψηφίων πρέπει να περιέχουν μόνο αριθμούς!");
        } catch (Exception e) {
            showErrorWindow("Σφάλμα συστήματος κατά τη φόρτωση του ημερολογίου.");
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        StringBuilder missingFields = new StringBuilder();

        if (titleField.getText().isBlank()) missingFields.append("- Τίτλος Θέματος\n");
        if (descriptionArea.getText().isBlank()) missingFields.append("- Περιγραφή\n");
        if (ectsField.getText().isBlank()) missingFields.append("- Απαιτούμενα ECTS\n");
        if (maxCandidatesField.getText().isBlank()) missingFields.append("- Μέγιστος Αριθμός Υποψηφίων\n");

        if (missingFields.length() > 0) {
            showErrorWindow("Δεν συμπληρώθηκαν τα υποχρεωτικά πεδία:\n" + missingFields.toString());
            return false;
        }
        return true;
    }

    private void showErrorWindow(String msg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();

            ErrorScreen errorScreen = loader.getController();
            errorScreen.setErrorMessage(msg);

            Stage stage = new Stage();
            stage.setTitle("Οθόνη Σφάλματος");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            if (errorLabel != null) errorLabel.setText(msg);
        }
    }
}