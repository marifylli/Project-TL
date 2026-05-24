package com.unipath.ui.common;

import com.unipath.controller.ManageCourseAddition;
import com.unipath.controller.ManageSecCourEditClass;
import com.unipath.controller.ManageStatAnalysis;
import com.unipath.ui.UC4.AnalysisFilterScreen;
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

    // uc 5
    @FXML
    public void clickAddCourse() {
        try {
            System.out.println("=== Έναρξη UC5: Προσθήκη Μαθήματος ===");
            new ManageCourseAddition();
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εκκίνηση του UC5:");
            e.printStackTrace();
        }
    }

    @FXML
    public void clickStats() {
        try {
            System.out.println("=== Έναρξη UC4: Στατιστικά & Αναφορές ===");
            ManageStatAnalysis controller = new ManageStatAnalysis();
            AnalysisFilterScreen filterScreen = new AnalysisFilterScreen(controller);
            filterScreen.displayFilterForm();
        } catch (Exception e) {
            System.err.println("❌ Σφάλμα κατά την εκκίνηση του UC4:");
            e.printStackTrace();
        }
    }
}

