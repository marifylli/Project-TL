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
        boolean success = false;

        try {
            // 2. Κλήση της μεθόδου του controller που αποθηκεύει σωστά με το UserSession ID
            success = controller.saveEvaluation(currentCourseName, rating, comments);

            if (success) {
                // Ενημέρωση στατιστικών μαθήματος
                controller.updateCourseStats(currentCourseName);
            }
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
        }

        // Κλείσιμο του τρέχοντος παραθύρου της φόρμας
        Stage currentStage = (Stage) ratingField.getScene().getWindow();
        currentStage.close();

        // 3. Εμφάνιση του ανάλογου Pop-up παραθύρου (Success / Error)
        try {
            String fxmlPath = success ? "/fxml/common/success-window-view.fxml" : "/fxml/common/error-window-view.fxml";
            java.net.URL popUpFxmlUrl = getClass().getResource(fxmlPath);

            if (popUpFxmlUrl == null) {
                fxmlPath = success ? "/fxml/common/success-window.fxml" : "/fxml/common/error-screen.fxml";
                popUpFxmlUrl = getClass().getResource(fxmlPath);
            }

            if (popUpFxmlUrl != null) {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(popUpFxmlUrl);
                javafx.scene.Parent root = loader.load();

                if (success) {
                    com.unipath.ui.common.SuccessScreen successController = loader.getController();
                    successController.setSuccessMessage("Η αξιολόγηση για το μάθημα υποβλήθηκε με επιτυχία στην SQLite!");
                } else {
                    com.unipath.ui.common.ErrorScreen errorController = loader.getController();
                    errorController.setErrorMessage("Αποτυχία υποβολής. Το ID του μαθήματος δεν βρέθηκε στη βάση.");
                }

                Stage popUpStage = new Stage();
                popUpStage.setTitle(success ? "Επιτυχία" : "Προσοχή / Σφάλμα");
                popUpStage.setScene(new javafx.scene.Scene(root));
                popUpStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
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

        Stage currentStage = (Stage) ratingField.getScene().getWindow();
        currentStage.close();
    }
}



