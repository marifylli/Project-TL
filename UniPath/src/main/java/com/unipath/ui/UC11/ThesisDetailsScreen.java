package com.unipath.ui.UC11;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.util.List;

public class ThesisDetailsScreen {
    private Thesis selectedThesis;
    private final ManageThesisInterest manager = new ManageThesisInterest();

    public void setThesis(Thesis thesis) { this.selectedThesis = thesis; }

    @FXML
    private void selectExpressInterest() { // Βήμα 5
        // Βήμα 6: Αναζήτηση κενών ωρών
        List<AvailabilitySlot> slots = manager.findAvailableSlots(selectedThesis.getProfessorId());

        if (slots.isEmpty()) {
            // Εναλλακτική ροή 8.α.2
            Alert alert = new Alert(Alert.AlertType.ERROR, "Δεν υπάρχουν διαθέσιμες ώρες.");
            alert.showAndWait();
        } else {
            // Μετάβαση στο ημερολόγιο
        }
    }
}