package com.unipath.ui.common;

import com.unipath.controller.ManageSecCourEditClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SecretaryMainScreen {

    @FXML private Label secretaryNameLabel;
    @FXML private Button editCourseButton;

    @FXML
    public void initialize() {
        secretaryNameLabel.setText("Καλωσήλθατε - Γραμματεία");
    }

    @FXML
    public void clickEditCourse() {
        try {
            System.out.println("=== Έναρξη UC6: Επεξεργασία Μαθήματος ===");
            new ManageSecCourEditClass();
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εκκίνηση του UC6:");
            e.printStackTrace();
        }
    }
}

