package com.unipath.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ManageThesisClass {

    public ManageThesisClass() {
    }

    // 1. ΑΚΡΙΒΩΣ ΟΠΩΣ ΤΟ CLASS DIAGRAM
    public boolean validateFieldsd(String title, String description, String ects, String maxCandidates) {
        if (title == null || title.isBlank() || description == null || description.isBlank() ||
                ects == null || ects.isBlank() || maxCandidates == null || maxCandidates.isBlank()) {

            highligthMissingFields("Σφάλμα: Δεν έχουν συμπληρωθεί όλα τα υποχρεωτικά πεδία!");
            return false;
        }
        return true;
    }

    // 2. ΑΚΡΙΒΩΣ ΟΠΩΣ ΤΟ CLASS DIAGRAM
    public void highligthMissingFields(String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();

            javafx.scene.control.Label lbl = (javafx.scene.control.Label) root.lookup("#errorLabel");
            if (lbl != null) lbl.setText(errorMessage);

            Stage stage = new Stage();
            stage.setTitle("ErrorScreen");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Σφάλμα φόρτωσης ErrorScreen: " + e.getMessage());
        }
    }
}