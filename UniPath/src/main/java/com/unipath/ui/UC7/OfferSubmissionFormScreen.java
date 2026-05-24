package com.unipath.ui.UC7;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.unipath.controller.ManageMentorProfile;
import com.unipath.model.StudentProfile;
import java.io.File;

public class OfferSubmissionFormScreen {

    @FXML private TextField fileRootTextField; // Αντιστοιχεί στο offerDataType()
    @FXML private Button submitOfferButton; // Αντιστοιχεί στο postOffer()
    @FXML private TextField meetingUrlTextField;

    private ManageMentorProfile controller;

    @FXML
    public void initialize() {
        controller = new ManageMentorProfile();
    }

    @FXML
    private void handleBrowseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Επιλέξτε Αρχείο Σημειώσεων");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Files", "*.docx", "*.doc")
        );

        Stage stage = (Stage) fileRootTextField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileRootTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleSubmit() {
        String notesFile = fileRootTextField.getText();
        String meetingUrl = meetingUrlTextField.getText();

        // 1. Περνάμε τα δεδομένα της φόρμας στον Controller
        controller.offerDataType(notesFile, meetingUrl);

        // 2.  Παίρνουμε το ID του συνδεδεμένου χρήστη από το UserSession
        int loggedInStudentId = com.unipath.login.UserSession.getInstance().getUserId();

        // 3. Δημιουργούμε το StudentProfile χρησιμοποιώντας το ΑΛΗΘΙΝΟ ID
        com.unipath.model.StudentProfile realProfile = new com.unipath.model.StudentProfile();
        realProfile.setStudentId(loggedInStudentId);

        // 4. Καλούμε την postOffer. Ο Controller θα αποθηκεύσει την προσφορά με το δικό σου ID στη βάση!
        controller.postOffer(realProfile);
    }

    @FXML
    private void handleBack() {
        controller.navigateToNewOfferForm();
    }
    public void display() {
        System.out.println("Εμφάνιση οθόνης: OfferSubmissionFormScreen");
    }
}