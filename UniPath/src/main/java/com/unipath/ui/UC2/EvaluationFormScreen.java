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


        if (!controller.checkFields(ratingText, comments)) {
            statusLabel.setText("Παρακαλώ δώστε έγκυρο βαθμό (1-5) και συμπληρώστε τα σχόλια!");
            return;
        }

        int rating = Integer.parseInt(ratingText);


        com.unipath.model.CourseEvaluation evaluation = new com.unipath.model.CourseEvaluation();
        evaluation.setStudentID(41);
        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setSubmissionDate(new java.util.Date());
        evaluation.setSubmitted(true);


        String courseId = "CEID_24Y332";
        if (currentCourseName != null) {
            if (currentCourseName.contains("Δεδομένων")) courseId = "CEID_24Y334";
            else if (currentCourseName.contains("Δίκτυα")) courseId = "CEID_24Y387";
        }
        evaluation.setCourseId(courseId);


        boolean success = false;
        try {
            com.unipath.repository.EvaluationRepository repo = new com.unipath.repository.EvaluationRepository();
            repo.saveEvaluation(evaluation);
            success = true;


            controller.updateCourseStats(currentCourseName);

        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την αποθήκευση: " + e.getMessage());
        }


        Stage currentStage = (Stage) ratingField.getScene().getWindow();
        currentStage.close();


        try {
            String fxmlPath = success ? "/fxml/common/success-window-view.fxml" : "/fxml/common/error-screen-view.fxml";
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



