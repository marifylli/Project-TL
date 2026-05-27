package com.unipath.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ManageProfCourseEdit {

    public ManageProfCourseEdit() {
    }


    public boolean checkFields(String title, String description) {
        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
                Parent root = loader.load();
                javafx.scene.control.Label lbl = (javafx.scene.control.Label) root.lookup("#errorLabel");
                if (lbl != null) lbl.setText("Σφάλμα: Τα υποχρεωτικά πεδία δεν έχουν συμπληρωθεί!");

                Stage stage = new Stage();
                stage.setTitle("ErrorScreen");
                stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                System.err.println("Σφάλμα φόρτωσης ErrorScreen: " + e.getMessage());
            }
            return false;
        }
        return true;
    }
}