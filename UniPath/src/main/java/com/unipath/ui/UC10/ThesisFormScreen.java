package com.unipath.ui.UC10;

import com.unipath.controller.ManageThesisClass;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ThesisFormScreen {


    private final ManageThesisClass manager = new ManageThesisClass();

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextArea prerequisitesArea;
    @FXML private TextField ectsField;
    @FXML private TextField maxCandidatesField;
    @FXML private TextArea requiredSkillsArea;

    public ThesisFormScreen() {}


    @FXML
    private void selectCalendar() {


        if (titleField.getText().trim().isEmpty() ||
                descriptionArea.getText().trim().isEmpty() ||
                ectsField.getText().trim().isEmpty() ||
                maxCandidatesField.getText().trim().isEmpty()) {

            System.out.println("[UC10] ❌ Εντοπίστηκαν ελλιπή πεδία. Ενεργοποίηση Εναλλακτικής Ροής [not all Fields].");

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/error-window-view.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("ErrorScreen");
                stage.setScene(new Scene(root));

                stage.showAndWait();

                System.out.println("[UC10] Η ροή επιστρέφει στο βήμα 3 της φόρμας.");
            } catch (IOException e) {
                System.err.println("❌ Σφάλμα κατά τη φόρτωση του error-window-view.fxml: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }


        System.out.println("[UC10] ✅ Όλα τα πεδία είναι συμπληρωμένα. Αποστολή requestCalendar() στον Controller.");
        manager.requestCalendar();
    }
}