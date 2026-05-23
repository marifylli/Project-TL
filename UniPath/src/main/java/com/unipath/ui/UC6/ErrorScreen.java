package com.unipath.ui.UC6;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorScreen {

    public ErrorScreen() {}

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
            Parent root = loader.load();

            com.unipath.ui.common.ErrorScreen controller = loader.getController();
            controller.setErrorMessage("Παρουσιάστηκε σφάλμα. Παρακαλώ ελέγξτε τα στοιχεία σας.");

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Σφάλμα");
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


