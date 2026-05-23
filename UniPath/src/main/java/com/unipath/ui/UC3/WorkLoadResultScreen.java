package com.unipath.ui.UC3;

import com.unipath.controller.ManageWorkLoadClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.net.URL;

public class WorkLoadResultScreen {

    @FXML private ListView<String> sortedCoursesListView;
    @FXML private Label workloadIndexLabel;
    @FXML private Button btnConfirm;

    private ManageWorkLoadClass logicController;

    public void setContext(ManageWorkLoadClass controller) {
        this.logicController = controller;

        // Εμφάνιση συνολικού δείκτη
        workloadIndexLabel.setText(String.format("%.1f", controller.getTotalWorkloadIndex()));

        // Εμφάνιση ταξινομημένων μαθημάτων
        sortedCoursesListView.getItems().clear();
        for (ManageWorkLoadClass.CourseWorkload cw : controller.getSortedCourses()) {
            sortedCoursesListView.getItems().add(cw.name + "  [Δείκτης: " + cw.workload + "]");
        }
    }

    @FXML
    private void onConfirmAnalysis() {
        // Κλήση της λογικής για αποθήκευση
        boolean saved = logicController.confirmAndSave();

        if (saved) {
            // Άνοιγμα του επίσημου Success Screen της ομάδας
            showSuccessWindow();
        }
    }

    private void showSuccessWindow() {
        try {
            // Φόρτωση του FXML της ομάδας
            URL fxmlUrl = getClass().getResource("/fxml/common/success-window-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/common/success-window-view.fxml");
            }

            if (fxmlUrl == null) {
                System.err.println("❌ Σφάλμα: Δεν βρέθηκε το αρχείο success-window-view.fxml!");
                return;
            }

            // Φορτώνουμε το layout της ομάδας καθαρά, χωρίς να πειράζουμε Controllers και Labels
            Parent root = FXMLLoader.load(fxmlUrl);

            // Αλλαγή της οθόνης στο ίδιο κεντρικό παράθυρο
            Stage stage = (Stage) btnConfirm.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Επιτυχής Ολοκλήρωση");
            stage.show();

            System.out.println("✓ Επιστροφή στην αρχική λειτουργία. Η οθόνη επιτυχίας φορτώθηκε!");

        } catch (Exception e) {
            System.err.println("❌ Απέτυχε η φόρτωση της οθόνης επιτυχίας:");
            e.printStackTrace();
        }
    }



}

