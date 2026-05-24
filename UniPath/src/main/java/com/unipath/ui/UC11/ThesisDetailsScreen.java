package com.unipath.ui.UC11;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.AvailabilitySlot;
import com.unipath.model.Thesis;
import com.unipath.login.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ThesisDetailsScreen {

    private Thesis selectedThesis;
    private final ManageThesisInterest manager = new ManageThesisInterest();

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label prerequisitesLabel;
    @FXML private Label ectsLabel;
    @FXML private Label skillsLabel;

    public ThesisDetailsScreen() {}

    // Μέθοδος αρχικοποίησης των δεδομένων της διπλωματικής από την προηγούμενη οθόνη (ThesisBoardScreen)
    public void setThesis(Thesis thesis) {
        this.selectedThesis = thesis;

        if (this.selectedThesis != null) {
            if (titleLabel != null) titleLabel.setText(selectedThesis.getTitle());
            if (descriptionLabel != null) descriptionLabel.setText(selectedThesis.getDescription());
            if (prerequisitesLabel != null) prerequisitesLabel.setText(selectedThesis.getPrerequisites());
            if (ectsLabel != null) ectsLabel.setText(String.valueOf(selectedThesis.getRequiredECTS()) + " ECTS");
            if (skillsLabel != null) skillsLabel.setText(selectedThesis.getRequiredSkills());
        }
    }

    /**
     * Βήμα 5: Εκδήλωση Ενδιαφέροντος
     * 🔥 Προστέθηκε το @FXML για να συνδεθεί με το OnAction του κουμπιού στο FXML!
     */
    @FXML
    private void selectExpressInterest() {
        // 1. Έλεγχος καταλληλότητας (το κρατάμε true για το testing)
        boolean isEligible = true;

        if (!isEligible) {
            showPopup("/fxml/common/error-window-view.fxml", "Απόρριψη: Δεν πληροίτε τις απαραίτητες ακαδημαϊκές προϋποθέσεις.");
            return;
        }

        if (selectedThesis == null) {
            System.out.println("[❌ ERROR] Δεν έχει επιλεγεί διπλωματική εργασία.");
            return;
        }

        System.out.println("[DEBUG] Επιλεγμένη Διπλωματική: " + selectedThesis.getTitle());
        System.out.println("[DEBUG] ID Καθηγητή που διαβάστηκε: " + selectedThesis.getProfessorId());

        // 2. Ανάκτηση των slots
        List<AvailabilitySlot> slots = manager.findAvailableSlots(selectedThesis.getProfessorId());

        // 3. Έλεγχος αν η βάση επέστρεψε δεδομένα
        if (slots == null || slots.isEmpty()) {
            System.out.println("[❌ ERROR] Η βάση δεν επέστρεψε κανένα slot για τον καθηγητή με ID: " + selectedThesis.getProfessorId());
            showPopup("/fxml/common/error-window-view.fxml", "Απόρριψη: Δεν υπάρχουν διαθέσιμες ώρες στη βάση για αυτόν τον καθηγητή.");
            return;
        }

        // 4. Άνοιγμα της οθόνης ημερολογίου με τα πραγματικά slots της βάσης
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Student/student-meeting-calendar-view.fxml"));
            Parent root = loader.load();

            StudentMeetingCalendarScreen calendarScreen = loader.getController();
            Stage currentStage = (Stage) titleLabel.getScene().getWindow();

            // Περνάμε τα αυθεντικά slots στην οθόνη
            calendarScreen.setCalendarContext(manager, selectedThesis, slots, currentStage);

            Stage stage = new Stage();
            stage.setTitle("Επιλογή Ώρας Συνέντευξης");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("❌ Σφάλμα κατά το φόρτωμα του student-meeting-calendar-view.fxml");
            e.printStackTrace();
        }
    }

    // Βοηθητική μέθοδος για την εμφάνιση των Modal Windows (Error Screens)
    private void showPopup(String fxmlPath, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Label lbl = (Label) root.lookup("#errorLabel");
            if (lbl != null) lbl.setText(message);

            Stage stage = new Stage();
            stage.setTitle("ErrorScreen");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την εμφάνιση του popup σφάλματος: " + e.getMessage());
        }
    }
}