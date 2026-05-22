package ui.UC2;

import controller.ManageEvaluation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EvaluationFormScreen {
    private Stage stage;
    private ManageEvaluation controller;
    private String courseId;

    public EvaluationFormScreen(Stage stage, ManageEvaluation controller, String courseId) {
        this.stage = stage;
        this.controller = controller;
        this.courseId = courseId;
    }

    public void display() {
        Label titleLabel = new Label("Αξιολόγηση Μαθήματος: " + courseId);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label ratingLabel = new Label("Βαθμολογία (1-5):");
        TextField ratingField = new TextField(); // Ή Slider, ανάλογα με το UI προτιμήσεων
        ratingField.setPromptText("π.χ. 5");

        Label commentLabel = new Label("Σχόλια:");
        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Εισάγετε τα σχόλιά σας εδώ...");

        Button submitButton = new Button("Υποβολή Αξιολόγησης");

        submitButton.setOnAction(e -> {
            String ratingText = ratingField.getText();
            String comments = commentArea.getText();

            // Βήμα 6: checkFields() στον controller
            boolean fieldsValid = controller.checkFields(ratingText, comments);

            if (!fieldsValid) {
                // Εναλλακτική Ροή: HighlightMissingFields()
                ratingField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                commentArea.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                System.out.println("Σφάλμα: Παρακαλώ συμπληρώστε όλα τα πεδία σωστά.");
            } else {
                // Βασική Ροή: Αποθήκευση και Ενημέρωση Στατιστικών
                int rating = Integer.parseInt(ratingText);
                controller.saveEvaluation(courseId, rating, comments);
                controller.updateCourseStats(courseId);

                // Εμφάνιση SuccessScreen (Κοινή κλάση του project)
                showSuccessScreen("Η αξιολόγησή σας υποβλήθηκε με επιτυχία! Ευχαριστούμε.");
            }
        });

        VBox layout = new VBox(12, titleLabel, ratingLabel, ratingField, commentLabel, commentArea, submitButton);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 450, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void showSuccessScreen(String message) {
        System.out.println("[SUCCESS SCREEN]: " + message);
    }
}


