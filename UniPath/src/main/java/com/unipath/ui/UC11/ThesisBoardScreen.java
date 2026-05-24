package com.unipath.ui.UC11;

import com.unipath.model.Thesis;
import com.unipath.repository.ThesisRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class ThesisBoardScreen {

    @FXML private ListView<Thesis> thesisListView;
    private final ThesisRepository thesisRepository = new ThesisRepository();

    @FXML
    public void initialize() {
        // Φόρτωση των διπλωματικών από τη βάση δεδομένων
        java.util.List<Thesis> listFromDb = thesisRepository.getAllTheses();
        thesisListView.setItems(FXCollections.observableArrayList(listFromDb));

        // 🔥 ΕΔΩ ΕΙΝΑΙ ΤΟ ΚΛΙΚ: Όταν ο φοιτητής επιλέγει μια διπλωματική από τη λίστα
        thesisListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("[UC11] Επιλέχθηκε η διπλωματική: " + newSelection.getTitle());
                openThesisDetails(newSelection);
            }
        });
    }

    private void openThesisDetails(Thesis selectedThesis) {
        try {
            // Προσπάθεια φόρτωσης του FXML των λεπτομερειών
            FXMLLoader loader = new FXMLLoader();
            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/thesis-details-view.fxml");

            // Αν δεν το βρει με το παραπάνω όνομα, δοκιμάζει εναλλακτικό κλασικό όνομα
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/fxml/Student/thesis-details-screen.fxml");
            }

            if (fxmlLocation == null) {
                throw new java.io.IOException("Δεν βρέθηκε το αρχείο FXML (thesis-details-view.fxml ή thesis-details-screen.fxml) στο φάκελο resources/fxml/Student/");
            }

            loader.setLocation(fxmlLocation);
            Parent root = loader.load();

            // Περνάμε τη διπλωματική στον Controller της οθόνης λεπτομερειών
            ThesisDetailsScreen detailsScreen = loader.getController();
            detailsScreen.setThesis(selectedThesis);

            // Αλλαγή της κεντρικής σκηνής (Scene) για να εμφανιστούν τα Details
            Stage stage = (Stage) thesisListView.getScene().getWindow();
            stage.getScene().setRoot(root);
            System.out.println("[UC11] Επιτυχής μετάβαση στην Οθόνη Θέματος Διπλωματικής (Details).");

        } catch (Exception e) {
            System.err.println("❌ Κρίσιμο Σφάλμα κατά τη μετάβαση στα Details: " + e.getMessage());
            e.printStackTrace();
        }
    }
}