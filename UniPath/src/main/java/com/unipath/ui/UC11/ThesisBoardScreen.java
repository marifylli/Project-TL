package com.unipath.ui.UC11;

import com.unipath.controller.ManageThesisInterest;
import com.unipath.model.Thesis;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ThesisBoardScreen {
    @FXML private ListView<Thesis> thesisListView;

    private final ManageThesisInterest manager = new ManageThesisInterest();

    @FXML
    public void initialize() {
        // Βήμα 2: Φόρτωση θεμάτων από τον Controller
        thesisListView.setItems(FXCollections.observableArrayList(manager.getAvailableTheses()));
    }

    @FXML
    private void onThesisSelected() {
        Thesis selected = thesisListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openThesisDetails(selected);
        }
    }

    private void openThesisDetails(Thesis thesis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Student/thesis-details-view.fxml"));
            Parent root = loader.load();

            // Περνάμε το θέμα στην οθόνη λεπτομερειών (Βήμα 4)
            ThesisDetailsScreen details = loader.getController();
            details.setThesis(thesis);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}