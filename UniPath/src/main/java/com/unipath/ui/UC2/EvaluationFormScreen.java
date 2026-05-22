package com.unipath.ui.UC2;

import com.unipath.controller.ManageEvaluation;
import com.unipath.ui.common.SuccessScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EvaluationFormScreen {

    @FXML private Label courseTitleLabel;
    @FXML private TextField ratingField;
    @FXML private TextArea commentArea;
    @FXML private Label errorLabel;

    private final ManageEvaluation controller = new ManageEvaluation();
    private String currentCourseId;
    private Stage previousStageReference;

    public void setEvaluationContext(String courseId, Stage previousStage) {
        this.currentCourseId = courseId;
        this.previousStageReference = previousStage;

        if (courseTitleLabel != null) {
            courseTitleLabel.setText("Μάθημα: " + courseId);
        }
    }


    @FXML
    private void onSubmitEvaluation() {
        errorLabel.setText("");
        String ratingText = ratingField.getText().trim();
        String comments = commentArea.getText().trim();

        // Βήμα 8 (sd2): checkFields() στον Controller
        if (!controller.checkFields(ratingText, comments)) {
            // Εναλλακτική Ροή 2: HighlightMissingFields / Σφάλμα
            errorLabel.setText("Συμπληρώστε σωστά όλα τα πεδία (Βαθμός 1-5)!");
            ratingField.setStyle("-fx-border-color: red;");
            return;
        }

        int rating = Integer.parseInt(ratingText);

        // Βήμα 9 (sd2): saveEvaluation()
        boolean isSaved = controller.saveEvaluation(currentCourseId, rating, comments);

        // Βήμα 11 (sd2): updateCourseStats()
        if (isSaved) {
            controller.updateCourseStats(currentCourseId);

            // Κλείσιμο παραθύρων
            if (previousStageReference != null) previousStageReference.close();
            ((Stage) ratingField.getScene().getWindow()).close();

            // Εμφάνιση SuccessScreen της ομάδας
            showSuccessWindow();
        } else {
            errorLabel.setText("Σφάλμα κατά την αποθήκευση της αξιολόγησης.");
        }
    }

    private void showSuccessWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();

            SuccessScreen successScreen = loader.getController();
            successScreen.setSuccessMessage("Η αξιολόγησή σας υποβλήθηκε με επιτυχία!");

            Stage stage = new Stage();
            stage.setTitle("Επιτυχία");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


