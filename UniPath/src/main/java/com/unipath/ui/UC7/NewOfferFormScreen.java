package com.unipath.ui.UC7;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;

public class NewOfferFormScreen {

    @FXML private ComboBox<String> courseComboBox; // Αντιστοιχεί στο queryCourses() και selectCourse()
    @FXML private ComboBox<String> helpTypeComboBox; // Αντιστοιχεί στο selectHelp()
    @FXML private Button nextButton;

    @FXML
    public void initialize() {
        // Εδώ θα φορτώνουν τα μαθήματα από τη βάση
    }

    public void display() {
        System.out.println("Εμφάνιση οθόνης: NewOfferFormScreen");
    }
}