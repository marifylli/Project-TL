package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label; // Προσθήκη για το dynamic label του error text
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThesisFormScreen {

    private final ManageThesisClass manager = new ManageThesisClass();

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextArea prerequisitesArea;
    @FXML private TextField ectsField;
    @FXML private TextField maxCandidatesField;
    @FXML private TextArea requiredSkillsArea;

    public ThesisFormScreen() {}

    @FXML
    private void selectCalendar() {
        // 1. Καθαρισμός προηγούμενων στυλ (επαναφορά στο κανονικό)
        titleField.setStyle("");
        descriptionArea.setStyle("");
        ectsField.setStyle("");
        maxCandidatesField.setStyle("");

        List<String> missingFields = new ArrayList<>();

        // 2. Έλεγχος κάθε υποχρεωτικού πεδίου ξεχωριστά και κοκκίνισμα αν είναι άδειο
        if (titleField.getText().trim().isEmpty()) {
            titleField.setStyle("-fx-border-color: red; -fx-border-width: 1.5px; -fx-border-radius: 4;");
            missingFields.add("Τίτλος Θέματος");
        }
        if (descriptionArea.getText().trim().isEmpty()) {
            descriptionArea.setStyle("-fx-border-color: red; -fx-border-width: 1.5px; -fx-border-radius: 4;");
            missingFields.add("Περιγραφή");
        }
        if (ectsField.getText().trim().isEmpty()) {
            ectsField.setStyle("-fx-border-color: red; -fx-border-width: 1.5px; -fx-border-radius: 4;");
            missingFields.add("Απαιτούμενα ECTS");
        }
        if (maxCandidatesField.getText().trim().isEmpty()) {
            maxCandidatesField.setStyle("-fx-border-color: red; -fx-border-width: 1.5px; -fx-border-radius: 4;");
            missingFields.add("Μέγιστος Αριθμός Υποψηφίων");
        }

        // 3. Αν βρέθηκαν άδεια πεδία, άνοιξε το Error Window με το σωστό μήνυμα
        if (!missingFields.isEmpty()) {
            System.out.println("[UC10] ❌ Εντοπίστηκαν ελλιπή πεδία. Ενεργοποίηση Εναλλακτικής Ροής [not all Fields].");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
                Parent root = loader.load();

                // Φτιάχνουμε το δυναμικό μήνυμα σφάλματος ανάλογα με το τι λείπει
                String errorMessage = "Λείπουν τα υποχρεωτικά πεδία:\n" + String.join(", ", missingFields);

                // Αναζητάμε το Label μέσα στο error window για να του αλλάξουμε το κείμενο δυναμικά
                // (Υποθέτοντας ότι το label σου στο error-window-view έχει fx:id="errorLabel" ή παρόμοιο)
                Label label = (Label) root.lookup("#errorLabel");
                if (label != null) {
                    label.setText(errorMessage);
                } else {
                    // Αν δεν έχει fx:id, ψάχνουμε οποιοδήποτε Label περιέχει το default κείμενο "Μήνυμα Σφάλματος"
                    for (javafx.scene.Node node : root.lookupAll("Label")) {
                        if (node instanceof Label && ((Label) node).getText().contains("Μήνυμα Σφάλματος")) {
                            ((Label) node).setText(errorMessage);
                            break;
                        }
                    }
                }

                Stage stage = new Stage();
                stage.setTitle("ErrorScreen");
                stage.setScene(new Scene(root));

                stage.showAndWait();

                System.out.println("[UC10] Η ροή επιστρέφει στο βήμα 3 της φόρμας.");
            } catch (IOException e) {
                System.err.println("❌ Σφάλμα κατά τη φόρτωση του error-window-view.fxml: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        System.out.println("[UC10] ✅ Όλα τα πεδία είναι συμπληρωμένα. Αποστολή requestCalendar() στον Controller.");
        manager.requestCalendar();
    }
}