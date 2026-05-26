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
            URL fxmlUrl = getClass().getResource("/fxml/common/success-window.fxml");

            if (fxmlUrl != null) {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                // Σύνδεση με τον Controller της ομάδας και πέρασμα του custom μηνύματος
                com.unipath.ui.common.SuccessScreen successController = loader.getController();
                successController.setSuccessMessage("Η προσομοίωση ολοκληρώθηκε και ο δείκτης φόρτου αποθηκεύτηκε με επιτυχία!");

                // Κλείνουμε το τρέχον μεγάλο παράθυρο αποτελεσμάτων
                Stage currentStage = (Stage) btnConfirm.getScene().getWindow();
                currentStage.close();

                // Ανοίγουμε το κοινό Success Screen ως αυτόνομο Pop-up Stage
                Stage popUpStage = new Stage();
                popUpStage.setTitle("UniPath - Επιτυχής Ολοκλήρωση");
                popUpStage.setScene(new Scene(root));
                popUpStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                popUpStage.show();

                System.out.println("[UI] Εμφανίστηκε το κοινό SuccessWindow επιτυχώς.");
            } else {
                System.err.println("Σφάλμα: Δεν βρέθηκε το αρχείο /fxml/common/success-window.fxml");
            }
        } catch (Exception e) {
            System.err.println("Απέτυχε η φόρτωση της οθόνης επιτυχίας: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void onCancelAnalysis() {
        System.out.println("Ακύρωση ανάλυσης. Επιστροφή στο Κεντρικό Μενού.");
        try {
            // Φορτώνουμε την αρχική οθόνη του φοιτητή
            URL fxmlUrl = getClass().getResource("/fxml/Student/student-main-screen.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Student/student-main-screen.fxml");
            }

            Parent root = FXMLLoader.load(fxmlUrl);

            // Παίρνουμε το τρέχον stage από το κουμπί επιβεβαίωσης
            Stage stage = (Stage) btnConfirm.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Κεντρικό Μενού");
            stage.show();
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την επιστροφή στο μενού: " + e.getMessage());
            e.printStackTrace();
        }
    }



}

