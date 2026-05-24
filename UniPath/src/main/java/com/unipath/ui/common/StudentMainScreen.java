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
    @FXML private Button workloadButton;
    @FXML private Button profileButton;
    @FXML private Button addNewOfferButton;
    @FXML private Button getHelpButton;
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

    @FXML
    public void clickAddNewOffer() {
        try {
            System.out.println("=== Έναρξη UC7: Προσθήκη Νέας Προσφοράς Βοήθειας (Add New Offer) ===");

            // Αρχικοποιούμε τον Controller του UC7
            com.unipath.controller.ManageMentorProfile mentorController = new com.unipath.controller.ManageMentorProfile();

            // Παίρνουμε το τρέχον Stage από ένα υπάρχον κουμπί (π.χ. το studyPlanButton)
            Stage stage = (Stage) studyPlanButton.getScene().getWindow();

            // Ξεκινάμε τη διαδικασία του UC7 περνώντας το stage
            mentorController.startMentorFlow(stage);

        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά την εκκίνηση του UC7 (Add New Offer):");
            e.printStackTrace();
        }
    }

    @FXML
    public void clickGetHelp() {
        try {
            System.out.println("=== Έναρξη UC8: Λήψη Ακαδημαϊκής Βοήθειας από Mentor ===");

            // Αρχικοποιούμε τον Controller του UC8
            com.unipath.controller.ManageGetHelp getHelpController = new com.unipath.controller.ManageGetHelp();

            // Παίρνουμε το τρέχον Stage και ξεκινάμε τη ροή (θα τη φτιάξουμε σε λίγο)
            Stage stage = (Stage) studyPlanButton.getScene().getWindow();

            // Θα καλέσουμε μια μέθοδο εκκίνησης στον Controller, π.χ. startGetHelpFlow(stage)
            // (Θα την προσθέσουμε στον Controller στο επόμενο βήμα)
            // getHelpController.startGetHelpFlow(stage);

            // Προσωρινά για δοκιμή, θα φορτώνουμε την πρώτη οθόνη του UC8
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/enrolled-courses-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/enrolled-courses-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Κέντρο Βοήθειας: Δηλωμένα Μαθήματα");
            stage.show();

        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά την εκκίνηση του UC8 (Λήψη Βοήθειας):");
            e.printStackTrace();
        }
    }

}

