package com.unipath.ui.common;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SuccessScreen {

    @FXML private Label successMessageLabel;

    // Μέθοδος για να αλλάζεις το μήνυμα ανάλογα με το Use Case (π.χ. "Η διπλωματική δημοσιεύτηκε!")
    public void setSuccessMessage(String message) {
        if (successMessageLabel != null) {
            successMessageLabel.setText(message);
        }
    }

    @FXML
    private void onConfirm() {
        Stage stage = (Stage) successMessageLabel.getScene().getWindow();
        stage.close();
    }
}