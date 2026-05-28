package com.unipath.ui.common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;

public class ErrorScreen {

    @FXML private Label errorMessageLabel;

    // --- ΤΟ ΚΕΝΤΡΙΚΟ ENUM ΓΙΑ ΟΛΑ ΤΑ ΣΦΑΛΜΑΤΑ ΤΟΥ PROJECT ---
    public enum ErrorType {
        // UC4 - Στατιστικά
        NO_STATISTICS_DATA,

        // UC5 - Δημιουργία / Ανάθεση Μαθήματος
        DUPLICATE_OR_EMPTY_COURSE,
        MAX_TEACHING_LOAD,

        // UC6 - Επεξεργασία Μαθήματος (Παράδειγμα με βάση την εικόνα σου)
        COURSE_NOT_FOUND,
        INVALID_COURSE_CHANGES
    }

    // Μέθοδος Controller
    public void setErrorMessage(String message) {
        if (errorMessageLabel != null) {
            errorMessageLabel.setText(message);
        }
    }

    // Μέθοδος Controller
    @FXML
    private void onClose() {
        Stage stage = (Stage) errorMessageLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Μετατρέπει το Enum στο αντίστοιχο Ελληνικό μήνυμα
     */
    private static String resolveMessage(ErrorType type) {
        return switch (type) {
            // UC4
            case NO_STATISTICS_DATA ->
                    "Δεν βρέθηκαν δεδομένα για τα επιλεγμένα φίλτρα. Παρακαλώ αλλάξτε τα κριτήρια αναζήτησης.";

            // UC5
            case DUPLICATE_OR_EMPTY_COURSE ->
                    "Τα στοιχεία του μαθήματος είναι ελλιπή ή το μάθημα υπάρχει ήδη στο σύστημα. Δοκιμάστε ξανά.";
            case MAX_TEACHING_LOAD ->
                    "Ο επιλεγμένος καθηγητής έχει συμπληρώσει τον μέγιστο επιτρεπτό διδακτικό φόρτο (3 μαθήματα).";

            // UC6 (Πρόσθεσε εδώ τα πραγματικά μηνύματα του UC6 σου)
            case COURSE_NOT_FOUND ->
                    "Το μάθημα που προσπαθείτε να επεξεργαστείτε δεν βρέθηκε.";
            case INVALID_COURSE_CHANGES ->
                    "Οι αλλαγές που εισάγατε δεν είναι έγκυρες. Παρακαλώ ελέγξτε τα πεδία.";
        };
    }

    /**
     * Η μοναδική στατική μέθοδος που ανοίγει το FXML
     */
    public static void show(Stage ownerStage, ErrorType type) {
        String message = resolveMessage(type);
        try {
            URL fxmlUrl = ErrorScreen.class.getResource("/fxml/common/error-window-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = ErrorScreen.class.getClassLoader().getResource("fxml/common/error-window-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            ErrorScreen controller = loader.getController();
            controller.setErrorMessage(message);

            Stage stage = new Stage();
            if (ownerStage != null) stage.initOwner(ownerStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("UniPath - Σφάλμα");
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            System.err.println("❌ Απέτυχε η εμφάνιση της κεντρικής οθόνης σφάλματος:");
            e.printStackTrace();
        }
    }
}