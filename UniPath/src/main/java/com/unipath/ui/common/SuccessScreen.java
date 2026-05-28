package com.unipath.ui.common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;

public class SuccessScreen {

    @FXML private Label successMessageLabel;

    // --- ΤΟ ΚΕΝΤΡΙΚΟ ENUM ΓΙΑ ΟΛΑ ΤΑ ΜΗΝΥΜΑΤΑ ΕΠΙΤΥΧΙΑΣ ΤΟΥ PROJECT ---
    public enum SuccessType {
        // UC5 - Προσθήκη / Ανάθεση Μαθήματος
        COURSE_ADDED_SUCCESSFULLY,


        COURSE_ADDED_AND_STUDENTS_NOTIFIED,

        // UC6 - Επεξεργασία Μαθήματος
        COURSE_UPDATED_SUCCESSFULLY
    }

    // Μέθοδος Controller: Θέτει το κείμενο στο Label του FXML
    public void setSuccessMessage(String message) {
        if (successMessageLabel != null) {
            successMessageLabel.setText(message);
        }
    }

    // Μέθοδος Controller: Κλείνει το παράθυρο όταν πατηθεί το OK/Επιβεβαίωση
    @FXML
    private void onConfirm() {
        Stage stage = (Stage) successMessageLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Μετατρέπει το Enum στο αντίστοιχο Ελληνικό μήνυμα επιτυχίας
     */
    private static String resolveMessage(SuccessType type) {
        return switch (type) {
            case COURSE_ADDED_SUCCESSFULLY ->
                    "Το μάθημα προστέθηκε επιτυχώς στο Πρόγραμμα Σπουδών!";

            case COURSE_UPDATED_SUCCESSFULLY ->
                    "Οι αλλαγές στο μάθημα αποθηκεύτηκαν με επιτυχία!";

            // 🌟 ΠΡΟΣΘΗΚΗ: Καλύπτουμε το νέο enum value για να σταματήσει το compile error
            case COURSE_ADDED_AND_STUDENTS_NOTIFIED ->
                    "Το μάθημα προστέθηκε επιτυχώς στο Πρόγραμμα Σπουδών!\nΑποστάλθηκε αυτόματα ειδοποίηση σε όλους τους φοιτητές.";
        };
    }

    /**
     * Η ΚΕΝΤΡΙΚΗ ΣΤΑΤΙΚΗ ΜΕΘΟΔΟΣ
     * Καλείται από οποιοδήποτε σημείο της εφαρμογής για να εμφανίσει ένα μήνυμα επιτυχίας.
     */
    public static void show(Stage ownerStage, SuccessType type) {
        String message = resolveMessage(type);
        try {
            // Εντοπισμός του κοινού FXML
            URL fxmlUrl = SuccessScreen.class.getResource("/fxml/common/success-window-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = SuccessScreen.class.getClassLoader().getResource("fxml/common/success-window-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println("❌ Σφάλμα: Δεν βρέθηκε το success-window-view.fxml!");
                return;
            }

            // Φόρτωση του FXML
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Λήψη του controller και ανάθεση του μηνύματος
            SuccessScreen controller = loader.getController();
            controller.setSuccessMessage(message);

            // Δημιουργία και ρύθμιση του Stage
            Stage stage = new Stage();
            if (ownerStage != null) {
                stage.initOwner(ownerStage);
            }
            stage.initModality(Modality.APPLICATION_MODAL); // Κλειδώνει την πίσω οθόνη
            stage.setScene(new Scene(root));
            stage.setTitle("UniPath - Επιτυχία");
            stage.setResizable(false);

            stage.showAndWait(); // Εμφάνιση και αναμονή

        } catch (Exception e) {
            System.err.println("❌ Απέτυχε η εμφάνιση της κεντρικής οθόνης επιτυχίας:");
            e.printStackTrace();
        }
    }
}