package com.unipath.ui.UC7;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class OfferSubmissionFormScreen {

    @FXML private TextField fileRootTextField; // Αντιστοιχεί στο offerDataType()
    @FXML private Button submitOfferButton; // Αντιστοιχεί στο postOffer()

    public void display() {
        System.out.println("Εμφάνιση οθόνης: OfferSubmissionFormScreen");
    }
}