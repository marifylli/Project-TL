


package com.unipath.ui.UC11;

import com.unipath.model.Thesis;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


public class ThesisBoardScreen {

    @FXML private ListView<Thesis> thesisListView;

    @FXML
    public void initialize() {

        java.util.List<com.unipath.model.Thesis> listFromDb = com.unipath.model.Thesis.findAllThesis();
        thesisListView.setItems(FXCollections.observableArrayList(listFromDb));


        thesisListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("[UC11] Επιλέχθηκε η διπλωματική: " + newSelection.getTitle());
                openThesisDetails(newSelection);
            }
        });
    }

    private void openThesisDetails(Thesis selectedThesis) {
        try {
            FXMLLoader loader = new FXMLLoader();
            java.net.URL fxmlLocation = getClass().getResource("/fxml/Student/thesis-details-view.fxml");

            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/fxml/Student/thesis-details-screen.fxml");
            }

            if (fxmlLocation == null) {
                throw new java.io.IOException("Δεν βρέθηκε το αρχείο FXML στο φάκελο resources/fxml/Student/");
            }

            loader.setLocation(fxmlLocation);
            Parent root = loader.load();

            ThesisDetailsScreen detailsScreen = loader.getController();


            Thesis fullDetails = Thesis.findThesisDetails(selectedThesis.getDiplomaticId());
            detailsScreen.setThesis(fullDetails != null ? fullDetails : selectedThesis);

            Stage stage = (Stage) thesisListView.getScene().getWindow();
            stage.getScene().setRoot(root);
            System.out.println("[UC11] Επιτυχής μετάβαση στην Οθόνη Details.");

        } catch (Exception e) {
            System.err.println("❌ Κρίσιμο Σφάλμα κατά τη μετάβαση στα Details: " + e.getMessage());
            e.printStackTrace();
        }
    }
}