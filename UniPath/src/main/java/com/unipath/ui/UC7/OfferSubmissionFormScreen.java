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
        // Αν ο controller δεν έχει περαστεί ακόμα, φτιάχνουμε έναν για να υπάρχει,
        // αλλά οι static τιμές (CourseID, HelpType) θα διαβαστούν κανονικά!
        if (controller == null) {
            controller = new ManageMentorProfile();
        }
        System.out.println("DEBUG UI: Η initialize() του OfferSubmissionFormScreen ολοκληρώθηκε.");
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
        if (controller == null) return;

        // Παίρνουμε τα κείμενα απευθείας από τα textfields της οθόνης
        String notesFile = fileRootTextField.getText() != null ? fileRootTextField.getText().trim() : "";
        String meetingUrl = meetingUrlTextField.getText() != null ? meetingUrlTextField.getText().trim() : "";

        // Τα στέλνουμε ρητά στον mentor controller
        controller.offerDataType(notesFile, meetingUrl);

        int loggedInStudentId = com.unipath.login.UserSession.getInstance().getUserId();

        com.unipath.model.StudentProfile realProfile = new com.unipath.model.StudentProfile();
        realProfile.setStudentId(loggedInStudentId);

        // Εκτέλεση δημοσίευσης
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