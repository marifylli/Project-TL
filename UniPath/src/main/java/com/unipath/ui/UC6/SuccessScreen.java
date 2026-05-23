package com.unipath.ui.UC6;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SuccessScreen {

    public SuccessScreen() {}

    public void display() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/success-window-view.fxml"));
            Parent root = loader.load();

            com.unipath.ui.common.SuccessScreen controller = loader.getController();
            controller.setSuccessMessage("Οι αλλαγές αποθηκεύτηκαν επιτυχώς και οι φοιτητές ειδοποιήθηκαν.");

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Επιτυχία");
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

