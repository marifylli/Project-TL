package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import com.unipath.model.Thesis;
import com.unipath.login.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    // Σύνδεση με τον Controller που έχει τις 2 μεθόδους του Class Diagram
    private final ManageThesisClass manager = new ManageThesisClass();

    // Αλλαγή σε selectCalendar() με κεφαλαίο C για να κουμπώσει με το OnAction του FXML σου
    @FXML
    public void selectCalendar() {
        try {
            int ects = ectsField.getText().trim().isEmpty() ? 0 : Integer.parseInt(ectsField.getText().trim());
            int maxCandidates = maxCandidatesField.getText().trim().isEmpty() ? 0 : Integer.parseInt(maxCandidatesField.getText().trim());

            Thesis temporaryThesis = new Thesis(
                    UserSession.getInstance().getUserId(),
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    prerequisitesArea.getText().trim(),
                    ects,
                    maxCandidates,
                    requiredSkillsArea.getText().trim()
            );

            // Κλήση του requestCalendar απευθείας από το Repository
            com.unipath.repository.ThesisRepository thesisRepo = new com.unipath.repository.ThesisRepository();
            thesisRepo.requestCalendar(UserSession.getInstance().getUserId());

            // Βήμα 6: Δημιουργία και εμφάνιση της MeetingCalendarScreen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Professor/meeting-calendar-view.fxml"));
            Parent root = loader.load();

            MeetingCalendarScreen calendarScreen = loader.getController();


            calendarScreen.setThesisContext(temporaryThesis, (Stage) titleField.getScene().getWindow());

            Stage stage = new Stage();
            stage.setTitle("Ημερολόγιο Συναντήσεων");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            manager.highligthMissingFields("Τα πεδία ECTS και Μέγιστος Αριθμός Υποψηφίων πρέπει να είναι έγκυροι αριθμοί!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}