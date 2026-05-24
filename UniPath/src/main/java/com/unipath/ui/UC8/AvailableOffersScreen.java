package com.unipath.ui.UC8;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.unipath.controller.ManageGetHelp;
import com.unipath.model.HelpOffer;
import java.util.List;
import java.util.ArrayList;

public class AvailableOffersScreen {

    @FXML private Label courseTitleLabel;
    @FXML private ListView<String> offersListView;
    @FXML private Button selectOfferButton;

    private ManageGetHelp controller;
    private String currentCourseId;

    // Λίστα για να κρατάμε τα πραγματικά αντικείμενα HelpOffer από τη βάση δεδομένων
    private final List<HelpOffer> displayedOffers = new ArrayList<>();

    @FXML
    public void initialize() {
        controller = new ManageGetHelp();
    }

    /**
     * Φορτώνει τις πραγματικές ενεργές προσφορές για το επιλεγμένο μάθημα από τη ΒΔ
     */
    public void loadOffersForCourse(String courseId, String courseTitle) {
        this.currentCourseId = courseId;
        courseTitleLabel.setText("Μάθημα: " + courseTitle);

        System.out.println("[UI-UC8] Ανάκτηση προσφορών από τη ΒΔ για το μάθημα: " + courseId);

        // Κλήση στον Controller (Βήμα 2 του SD) που καλεί το HelpOfferRepository
        List<HelpOffer> activeOffers = controller.getAvailableOffers(courseId);

        // Καθαρισμός της λίστας και του UI πριν το γέμισμα
        offersListView.getItems().clear();
        displayedOffers.clear();

        if (activeOffers != null && !activeOffers.isEmpty()) {
            for (HelpOffer offer : activeOffers) {
                // Εμφανίζουμε το ID του Μέντορα και τον Τύπο Βοήθειας στην οθόνη
                String rowText = "Mentor ID: " + offer.getMentorId() + " -> Τύπος: " + offer.getHelpType();
                offersListView.getItems().add(rowText);
                displayedOffers.add(offer); // Αποθήκευση του αντικειμένου για το επόμενο βήμα
            }
        } else {
            System.out.println("[UI-UC8] Εναλλακτική Ροή: Δεν βρέθηκε καμία ενεργή προσφορά για αυτό το μάθημα.");
            offersListView.setPlaceholder(new Label("Δεν υπάρχουν διαθέσιμες προσφορές για αυτό το μάθημα."));
        }
    }

    @FXML
    private void handleSelectOffer() {
        // Παίρνουμε τη θέση της επιλεγμένης γραμμής από τη ListView
        int selectedIndex = offersListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            System.out.println("[UI-UC8] Σφάλμα: Δεν επιλέχθηκε καμία προσφορά από τη λίστα.");
            return;
        }

        // Ανάκτηση του πραγματικού HelpOffer αντικειμένου
        HelpOffer selectedOffer = displayedOffers.get(selectedIndex);
        System.out.println("[UI-UC8] Ο φοιτητής επέλεξε την προσφορά του Mentor: " + selectedOffer.getMentorId());

        // 🚀 ΜΕΤΑΒΑΣΗ ΣΤΟ ΒΗΜΑ 3: HelpServicesScreen
        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/help-services-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/help-services-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Περνάμε το πραγματικό HelpOffer στην επόμενη οθόνη
            HelpServicesScreen nextScreen = loader.getController();
            nextScreen.loadOfferDetails(selectedOffer);

            Stage stage = (Stage) selectOfferButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Παροχές Βοήθειας");
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά τη μετάβαση στο HelpServicesScreen:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            java.net.URL fxmlUrl = getClass().getResource("/fxml/Student/enrolled-courses-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/enrolled-courses-view.fxml");
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) offersListView.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Κέντρο Βοήθειας: Δηλωμένα Μαθήματα");
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την επιστροφή στην οθόνη μαθημάτων: " + e.getMessage());
        }
    }
}