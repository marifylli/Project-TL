package com.unipath.ui.UC11;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.Thesis;
import com.unipath.login.UserSession; // Πρόσθεσε αυτό το import
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import com.unipath.ui.UC10.MeetingCalendarScreen;

public class ThesisDetailsScreen {

    private Thesis selectedThesis;
    private final ManageThesisInterest manager = new ManageThesisInterest();

    public void setThesis(Thesis thesis) {
        this.selectedThesis = thesis;
    }

    @FXML
    private void handleExpressInterest() {
        // Χρήση του σωστού ID (άλλαξέ το αν στο Thesis.java λέγεται διαφορετικά)
        int thesisId = Integer.parseInt(selectedThesis.getCourseID());
        int studentId = UserSession.getInstance().getUserId();

        // Βήματα 6-7: Έλεγχος κριτηρίων
        if (manager.checkEligibility(studentId, thesisId)) {
            // Αν πληροί τα κριτήρια -> Βήμα 8 (Άνοιγμα Ημερολογίου)
            openCalendarView(selectedThesis.getProfessorId());
        } else {
            // Βήμα 7.α: Εναλλακτική ροή απόρριψης
            showRejectionScreen("Δεν πληροίτε τις ακαδημαϊκές προϋποθέσεις για αυτό το θέμα.");
        }
    }

    // Η μέθοδος που έλειπε
    private void showRejectionScreen(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Απόρριψη");
        alert.setHeaderText("Μη επιλέξιμο θέμα");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openCalendarView(int professorId) {
        // Εδώ θα υλοποιήσεις τη φόρτωση του MeetingCalendarScreen (Βήμα 8)
    }
}