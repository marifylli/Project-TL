package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.Thesis;
import com.unipath.login.UserSession;
import com.unipath.ui.common.ErrorScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ThesisFormScreen {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextArea prerequisitesArea;
    @FXML private TextField ectsField;
    @FXML private TextField maxCandidatesField;
    @FXML private TextArea requiredSkillsArea;

    private final ManageThesisClass manager = new ManageThesisClass();

    // Βήμα 4: selectCalendar()
    @FXML
    private void selectCalendar() {
        // Βήμα 9: Το Σύστημα επικυρώνει τη συμπλήρωση (validateFields) ΠΡΙΝ προχωρήσει
        if (!validateFields()) {
            return; // alt [not all Fields]: Η ροή διακόπτεται και μένει στη φόρμα (Βήμα 3)
        }

        try {
            int ects = Integer.parseInt(ectsField.getText().trim());
            int maxCandidates = Integer.parseInt(maxCandidatesField.getText().trim());

            Thesis temporaryThesis = new Thesis(
                    UserSession.getInstance().getUserId(),
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    prerequisitesArea.getText().trim(),
                    ects,
                    maxCandidates,
                    requiredSkillsArea.getText().trim()
            );

            // Βήμα 5: requestCalendar() στον Controller
            manager.requestCalendar(UserSession.getInstance().getUserId());

            // Βήμα 6: Δημιουργία και εμφάνιση της MeetingCalendarScreen (display)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/meeting-calendar-view.fxml"));
            Parent root = loader.load();

            MeetingCalendarScreen calendarScreen = loader.getController();
            calendarScreen.setThesisContext(temporaryThesis, (Stage) titleField.getScene().getWindow());

            Stage stage = new Stage();
            stage.setTitle("Ημερολόγιο Συναντήσεων");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            highligthMissingFields("Τα πεδία ECTS και Μέγιστος Αριθμός Υποψηφίων πρέπει να είναι έγκυροι αριθμοί!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Εσωτερικό μήνυμα validateFields() του διαγράμματος
    public boolean validateFields() {
        if (titleField.getText() == null || titleField.getText().isBlank() ||
                descriptionArea.getText() == null || descriptionArea.getText().isBlank() ||
                ectsField.getText() == null || ectsField.getText().isBlank() ||
                maxCandidatesField.getText() == null || maxCandidatesField.getText().isBlank()) {

            // alt [not all Fields] -> trigger οθόνης σφάλματος
            highligthMissingFields("Δεν συμπληρώθηκαν όλα τα υποχρεωτικά πεδία!");
            return false;
        }
        return true;
    }

    // Μήνυμα highligthMissingFields() στην ErrorScreen (alt [not all Fields])
    private void highligthMissingFields(String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();

            ErrorScreen errorScreen = loader.getController();
            errorScreen.setErrorMessage(errorMessage);

            Stage errorStage = new Stage();
            errorStage.setTitle("Σφάλμα Συμπλήρωσης");
            errorStage.setScene(new Scene(root));

            // 9.α.3: Modal για να κλειδώσει η πίσω οθόνη μέχρι να πατηθεί το "κλείσιμο"
            errorStage.initModality(Modality.APPLICATION_MODAL);
            errorStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}