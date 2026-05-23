package com.unipath.ui.common;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.ui.UC2.CoursesToEvaluateScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;

public class StudentMainScreen {

    @FXML private Label studentNameLabel;
    @FXML private Label coursesCountLabel;
    @FXML private VBox coursesContainer;
    @FXML private Button evaluateButton;
    @FXML private Button studyPlanButton;
    @FXML private Button profileButton;

    @FXML
    public void initialize() {
        studentNameLabel.setText("Καλωσήλθατε στο Κεντρικό Μενού");
    }

    @FXML
    public void clickEvaluateCourses() {
        try {
            System.out.println("=== Έναρξη UC2: Φόρτωση Λίστας Μαθημάτων ===");

            // Στοχεύουμε κατευθείαν το νέο αρχείο που φτιάξαμε στον φάκελο Student
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/courses-evaluate-view.fxml");

            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/courses-evaluate-view.fxml");
            }

            if (fxmlUrl == null) {
                System.err.println("❌ Κρίσιμο Σφάλμα: Το αρχείο courses-evaluate-view.fxml δεν βρέθηκε!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Αλλαγή σκηνής στο κεντρικό παράθυρο
            Stage stage = (Stage) evaluateButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Λίστα Μαθημάτων προς Αξιολόγηση");
            stage.show();

        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά τη μετάβαση στην οθόνη UC2:");
            e.printStackTrace();
        }
    }

    @FXML
    public void clickCreatePlan() {
        try {
            System.out.println("=== Έναρξη UC1: Δημιουργία και Έλεγχος Πλάνου Σπουδών ===");

            // Αρχικοποιούμε τον σωστό controller για το UC1 (ManageStudyPlan)
            com.unipath.controller.ManageStudyPlan studyPlanController = new com.unipath.controller.ManageStudyPlan();

            // Εκκινούμε τη διαδικασία, η οποία θα φορτώσει το scenario-selection-view.fxml
            studyPlanController.startCreatePlan();

        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εκκίνηση του UC1 (ManageStudyPlan):");
            e.printStackTrace();
        }
    }

    @FXML
    public void clickfortosioannas() {
        try {
            System.out.println("=== Έναρξη UC3: Προσομοίωση και Εξισορρόπηση Φόρτου Εργασίας ===");

            // Αρχικοποιούμε τον ολοκαίνουργιο controller του UC3
            com.unipath.controller.ManageWorkLoadClass workloadController = new com.unipath.controller.ManageWorkLoadClass();

            // Παίρνουμε το τρέχον Stage και ξεκινάμε την ανάλυση
            Stage stage = (Stage) studyPlanButton.getScene().getWindow();
            workloadController.startAnalysis(stage);

        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εκκίνηση του UC3:");
            e.printStackTrace();
        }
    }



}

