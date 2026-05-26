package com.unipath.ui.UC2;

import com.unipath.controller.ManageEvaluation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EvaluationFormScreen {

    @FXML private Label courseLabel;
    @FXML private TextField ratingField;
    @FXML private TextArea commentsArea;
    @FXML private Label statusLabel;

    private String currentCourseName;
    private Stage previousStage;
    private final ManageEvaluation controller = new ManageEvaluation();

    // Αυτή τη μέθοδο την καλεί ο προηγούμενος controller για να στείλει το μάθημα
    public void setEvaluationContext(String courseName, Stage stage) {
        this.currentCourseName = courseName;
        this.previousStage = stage;
        if (courseLabel != null) {
            courseLabel.setText("Μάθημα: " + courseName);
        }
    }

    @FXML
    private void onSubmit() {
        statusLabel.setText("");
        String ratingText = ratingField.getText();
        String comments = commentsArea.getText();

        // 1. Έλεγχος εγκυρότητας πεδίων μέσω του controller
        if (!controller.checkFields(ratingText, comments)) {
            statusLabel.setText("Παρακαλώ δώστε έγκυρο βαθμό (1-5) και συμπληρώστε τα σχόλια!");
            return;
        }

        int rating = Integer.parseInt(ratingText);

        // Δημιουργία και γέμισμα του αντικειμένου της ομάδας
        com.unipath.model.CourseEvaluation evaluation = new com.unipath.model.CourseEvaluation();
        evaluation.setStudentID(41); // Κλειδωμένο ID για το test παρουσίασης
        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setSubmissionDate(new java.util.Date());
        evaluation.setSubmitted(true);

        // Αντιστοίχιση του String ID του μαθήματος με βάση το UI
        String courseId = "CEID_24Y332";
        if (currentCourseName != null) {
            if (currentCourseName.contains("Δεδομένων")) courseId = "CEID_24Y334";
            else if (currentCourseName.contains("Δίκτυα")) courseId = "CEID_24Y387";
        }
        evaluation.setCourseId(courseId);

        // Αποθήκευση στη βάση δεδομένων μέσω του Repository
        boolean success = false;
        try {
            com.unipath.repository.EvaluationRepository repo = new com.unipath.repository.EvaluationRepository();
            repo.saveEvaluation(evaluation);
            success = true;
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
        }

        // Κλείνουμε αμέσως την τρέχουσα φόρμα αξιολόγησης
        Stage currentStage = (Stage) ratingField.getScene().getWindow();
        currentStage.close();

        // ΕΜΦΑΝΙΣΗ COMMON POP-UPS (SUCCESS / ERROR) ──────────────────
        try {
            String fxmlPath = success ? "/fxml/common/success-window-view.fxml" : "/fxml/common/error-screen-view.fxml";
            java.net.URL popUpFxmlUrl = getClass().getResource(fxmlPath);

            if (popUpFxmlUrl == null) {
                // Fallback αν τα fxml αρχεία της ομάδας σας δεν έχουν το επίθεμα "-view"
                fxmlPath = success ? "/fxml/common/success-window.fxml" : "/fxml/common/error-screen.fxml";
                popUpFxmlUrl = getClass().getResource(fxmlPath);
            }

            if (popUpFxmlUrl != null) {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(popUpFxmlUrl);
                javafx.scene.Parent root = loader.load();

                // Περνάμε το custom μήνυμα χρησιμοποιώντας τις επίσημες μεθόδους της ομάδας σας!
                if (success) {
                    com.unipath.ui.common.SuccessScreen successController = loader.getController();
                    successController.setSuccessMessage("Η αξιολόγηση για το μάθημα υποβλήθηκε με επιτυχία στην SQLite!");
                } else {
                    com.unipath.ui.common.ErrorScreen errorController = loader.getController();
                    errorController.setErrorMessage("Αποτυχία υποβολής. Το ID του μαθήματος δεν βρέθηκε στη βάση.");
                }

                // Άνοιγμα του pop-up παραθύρου
                Stage popUpStage = new Stage();
                popUpStage.setTitle(success ? "Επιτυχία" : "Προσοχή / Σφάλμα");
                popUpStage.setScene(new javafx.scene.Scene(root));
                popUpStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Κλειδώνει την πίσω οθόνη
                popUpStage.show();
                System.out.println("✓ [UI] Εμφανίστηκε το κοινό παράθυρο επιτυχώς.");
            } else {
                System.err.println("Δεν βρέθηκε το FXML αρχείο στο path: " + fxmlPath);
            }
        } catch (Exception e) {
            System.err.println("Αποτυχία φόρτωσης του common screen: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void onCancel() {
        // Κλείνει απλά τη φόρμα και επιστρέφει
        Stage currentStage = (Stage) ratingField.getScene().getWindow();
        currentStage.close();
    }
}



