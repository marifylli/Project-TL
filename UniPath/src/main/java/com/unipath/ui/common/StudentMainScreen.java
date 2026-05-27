package com.unipath.ui.common;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.ui.UC2.CoursesToEvaluateScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class StudentMainScreen {

    @FXML private Label studentNameLabel;
    @FXML private Label coursesCountLabel;
    @FXML private VBox coursesContainer;
    @FXML private Button evaluateButton;
    @FXML private Button studyPlanButton;
    @FXML private Button workloadButton;
    @FXML private Button profileButton;
    @FXML private Button addNewOfferButton;
    @FXML private Button getHelpButton;
    @FXML private Button thesisBoardButton;

    @FXML
    public void initialize() {
        studentNameLabel.setText("Καλωσήλθατε στο Κεντρικό Μενού");
        loadSubmittedStudyPlans();
    }

    public void loadSubmittedStudyPlans() {
        if (coursesContainer == null) return;

        coursesContainer.getChildren().clear();
        int studentId = com.unipath.login.UserSession.getInstance().getUserId();
        System.out.println("DEBUG MAIN SCREEN: Ανάκτηση πλάνων για studentId = " + studentId);

        String sql = "SELECT sp.scenarioId, s.title FROM StudyPlan sp " +
                "LEFT JOIN Scenario s ON sp.scenarioId = s.scenarioId " +
                "WHERE sp.studentId = ? AND (sp.isFinalized = 1 OR sp.status = 'Finalized')";

        int planCount = 0;

        try (java.sql.Connection conn = com.unipath.dataBase.DBManager.getInstance().connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    planCount++;
                    String scenarioTitle = rs.getString("title");
                    int scenarioId = rs.getInt("scenarioId");

                    if (scenarioTitle == null || scenarioTitle.isEmpty()) {
                        if (scenarioId == 1) scenarioTitle = "Σενάριο 1: 1 Κύρια Κατεύθυνση";
                        else if (scenarioId == 2) scenarioTitle = "Σενάριο 2: Διπλή Κατεύθυνση";
                        else if (scenarioId == 3) scenarioTitle = "Σενάριο 3: Γενική Κατεύθυνση";
                        else scenarioTitle = "Οριστικοποιημένο Πλάνο Σπουδών";
                    }

                    Label planLabel = new Label("✓ " + scenarioTitle);
                    planLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32; -fx-font-weight: bold; -fx-padding: 5px;");
                    coursesContainer.getChildren().add(planLabel);
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("❌ Σφάλμα κατά τη φόρτωση των πλάνων: " + e.getMessage());
            e.printStackTrace();
        }

        if (coursesCountLabel != null) {
            coursesCountLabel.setText("Ενεργά Πλάνα Σπουδών: " + planCount);
        }

        if (planCount == 0) {
            Label noPlanLabel = new Label("Δεν έχετε δημιουργήσει ακόμα κάποιο Πλάνο Σπουδών.");
            noPlanLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #757575; -fx-padding: 5px;");
            coursesContainer.getChildren().add(noPlanLabel);
        }
    }

    @FXML
    public void clickCreatePlan() {
        try {
            System.out.println("=== Έναρξη UC1: Δημιουργία Πλάνου (Ασφαλές Popup Mode) ===");

            com.unipath.controller.ManageStudyPlan studyPlanController = new com.unipath.controller.ManageStudyPlan();

            java.net.URL fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/scenario-selection-view.fxml");
            if (fxmlUrl == null) fxmlUrl = getClass().getResource("/fxml/Student/scenario-selection-view.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            com.unipath.ui.UC1.ScenarioSelectionScreen scenarioScreen = loader.getController();
            scenarioScreen.setManageStudyPlan(studyPlanController);

            // 🌟 ΔΗΜΙΟΥΡΓΙΑ ΝΕΟΥ ΑΥΤΟΝΟΜΟΥ ΠΑΡΑΘΥΡΟΥ (STAGE)
            Stage popupStage = new Stage();
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Κλειδώνει το πίσω παράθυρο για ασφάλεια
            popupStage.setScene(new Scene(root, 650, 500));
            popupStage.setTitle("Επιλογή Σεναρίου - UniPath");

            // Αποθηκεύουμε αυτό το νέο παράθυρο στον controller της ομάδας σου
            try {
                java.lang.reflect.Field stageField = studyPlanController.getClass().getDeclaredField("mainStage");
                stageField.setAccessible(true);
                stageField.set(studyPlanController, popupStage);
            } catch (Exception ignored) {}

            // 🌟 ΜΟΛΙΣ ΚΛΕΙΣΕΙ ΤΟ POPUP (Είτε από υποβολή είτε από ακύρωση), ΑΝΑΝΕΩΝΟΥΜΕ ΤΟ ΜΕΝΟΥ!
            popupStage.setOnHiding(event -> {
                System.out.println("✓ Το Popup έκλεισε. Ανανέωση δεδομένων Κεντρικής Οθόνης...");
                loadSubmittedStudyPlans();
            });

            popupStage.showAndWait(); // Εμφάνιση και αναμονή

        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εκκίνηση του πλάνου: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void clickfortosioannas() {
        try {
            System.out.println("=== Έναρξη UC3: Ανάλυση Φόρτου Εργασίας ===");
            com.unipath.controller.ManageWorkLoadClass workloadController = new com.unipath.controller.ManageWorkLoadClass();

            Stage stage = (Stage) studyPlanButton.getScene().getWindow();
            workloadController.startAnalysis(stage);
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα στο UC3: " + e.getMessage());
        }
    }

    @FXML
    public void clickEvaluateCourses() {
        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/courses-evaluate-view.fxml");
            if (fxmlUrl == null) fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/courses-evaluate-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Stage stage = (Stage) evaluateButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Λίστα Μαθημάτων προς Αξιολόγηση");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickAddNewOffer() {
        try {
            com.unipath.controller.ManageMentorProfile mentorController = new com.unipath.controller.ManageMentorProfile();
            Stage stage = (Stage) studyPlanButton.getScene().getWindow();
            mentorController.startMentorFlow(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickGetHelp() {
        try {
            Stage stage = (Stage) studyPlanButton.getScene().getWindow();
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/enrolled-courses-view.fxml");
            if (fxmlUrl == null) fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/enrolled-courses-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Κέντρο Βοήθειας");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickThesisBoard() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/Student/thesis-board-view.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) studyPlanButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}