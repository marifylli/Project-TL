package com.unipath.ui.common;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorScreen {

    @FXML private Label errorMessageLabel;

    // Μέθοδος που επιτρέπει σε οποιοδήποτε Use Case να "περάσει" το δικό του custom μήνυμα
    public void setErrorMessage(String message) {
        if (errorMessageLabel != null) {
            errorMessageLabel.setText(message);
        }
    }

    // Αντιστοιχεί στο Βήμα 9.α.3 του Sequence Diagram (Κλείσιμο μηνύματος)
    @FXML
    private void onClose() {
        Stage stage = (Stage) errorMessageLabel.getScene().getWindow();
        stage.close();
    }
}