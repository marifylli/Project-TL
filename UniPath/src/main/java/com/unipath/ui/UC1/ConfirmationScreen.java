package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConfirmationScreen {

        private ManageStudyPlan manageStudyPlan;
        @FXML private Label statusTitle;
        @FXML private Label messageLabel;

        public ConfirmationScreen() {
        }

        public ConfirmationScreen(ManageStudyPlan manageStudyPlan) {
            this.manageStudyPlan = manageStudyPlan;
        }

        public void setConfirmationData(ManageStudyPlan manageStudyPlan, boolean isValid) {
                this.manageStudyPlan = manageStudyPlan;
                displayConfirmation();

                // Δυναμική αλλαγή κειμένου/στυλ ανάλογα με την εγκυρότητα του πλάνου
                if (isValid) {
                        statusTitle.setText("✓ Επιτυχής Υποβολή Πλάνου!");
                        statusTitle.setStyle("-fx-text-fill: #2ecc71;");
                        messageLabel.setText("Το πρόγραμμα σπουδών σας αποθηκεύτηκε επιτυχώς καθώς πληροί όλους τους κανόνες.");
                } else {
                        statusTitle.setText("✕ Αποτυχία Κανόνων");
                        statusTitle.setStyle("-fx-text-fill: #e74c3c;");
                        messageLabel.setText("Το πρόγραμμα σπουδών απορρίφθηκε. Παρακαλώ ελέγξτε αν έχετε ακριβώς 17 μαθήματα και 270 ECTS.");
                }
        }

        public void displayConfirmation() {

        }

        @FXML
        private void handleClose() {
                if (statusTitle != null && statusTitle.getScene() != null) {
                        statusTitle.getScene().getWindow().hide();
                }
        }


}
