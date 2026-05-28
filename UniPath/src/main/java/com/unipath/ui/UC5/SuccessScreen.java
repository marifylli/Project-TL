package com.unipath.ui.UC5;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class SuccessScreen {

    public void display() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/common/success-window-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/common/success-window-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println("Σφάλμα: Δεν βρέθηκε το success-window-view.fxml!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();


            com.unipath.ui.common.SuccessScreen controller = loader.getController();
            controller.setSuccessMessage("Το μάθημα προστέθηκε επιτυχώς στο Πρόγραμμα Σπουδών!");

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("UniPath - Επιτυχής Προσθήκη");
            stage.showAndWait();

        } catch (Exception e) {
            System.err.println(" Απέτυχε η φόρτωση της οθόνης επιτυχίας:");
            e.printStackTrace();
        }
    }
}