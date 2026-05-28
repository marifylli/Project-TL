package com.unipath.ui.UC8;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.unipath.controller.ManageGetHelp;
import com.unipath.model.HelpOffer;

public class HelpServicesScreen {

    @FXML private Label mentorLabel;
    @FXML private Label typeLabel;
    @FXML private Label detailsLabel;
    @FXML private Button actionButton;

    private ManageGetHelp controller;
    private HelpOffer currentOffer;

    @FXML
    public void initialize() {
        controller = new ManageGetHelp();
    }

    public void loadOfferDetails(HelpOffer offer) {
        this.currentOffer = offer;
        controller.getOfferServices(offer);

        mentorLabel.setText("Mentor ID: " + offer.getMentorId());
        typeLabel.setText("Τύπος Βοήθειας: " + offer.getHelpType());

        if (offer.getNotesFile() != null && !offer.getNotesFile().isBlank()) {
            detailsLabel.setText("Αρχείο Σημειώσεων: " + offer.getNotesFile());
            actionButton.setText("📥 Λήψη Αρχείου Σημειώσεων");
        } else if (offer.getMeetingUrl() != null && !offer.getMeetingUrl().isBlank()) {
            detailsLabel.setText("Σύνδεσμος Τηλεδιάσκεψης Microsoft Teams");
            actionButton.setText("🌐 Σύνδεση σε Meeting");
        } else {
            detailsLabel.setText("Δεν υπάρχουν διαθέσιμες ψηφιακές παροχές.");
            actionButton.setDisable(true);
        }
    }

    @FXML
    private void handleExecuteAction() {
        System.out.println("[UI-UC8] Εκτέλεση ενέργειας παροχής.");
        // Παίρνουμε το τρέχον Stage από το κουμπί που πατήθηκε
        Stage stage = (Stage) actionButton.getScene().getWindow();
        controller.executeAction(stage);
    }
    @FXML
    private void handleFinalize() {
        System.out.println("[UI-UC8] Ολοκλήρωση συνεδρίας.");


        System.out.println("[UI-UC8] Το ιστορικό του φοιτητή ενημερώθηκε επιτυχώς.");

        returnToMain();
    }

    @FXML
    private void handleBack() {
        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/available-offers-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/available-offers-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            AvailableOffersScreen prevScreen = loader.getController();
            prevScreen.loadOffersForCourse(currentOffer.getCourseId(), "Επιστροφή");

            Stage stage = (Stage) mentorLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Διαθέσιμες Προσφορές");
        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά την επιστροφή: " + e.getMessage());
        }
    }

    private void returnToMain() {
        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/student-main-screen.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/student-main-screen.fxml");
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) mentorLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Κεντρικό Μενού");
        } catch (Exception e) {
            System.err.println(" Σφάλμα κατά την επιστροφή στο κύριο μενού: " + e.getMessage());
        }
    }
}