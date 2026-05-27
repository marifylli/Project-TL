package com.unipath.ui.common;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorScreen {

    @FXML private Label errorMessageLabel;


    public void setErrorMessage(String message) {
        if (errorMessageLabel != null) {
            errorMessageLabel.setText(message);
        }
    }


    @FXML
    private void onClose() {
        Stage stage = (Stage) errorMessageLabel.getScene().getWindow();
        stage.close();
    }
}