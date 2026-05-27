package com.unipath.ui.UC3;

import com.unipath.controller.ManageWorkLoadClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;

public class WorkLoadResultScreen {

    @FXML private ListView<String> sortedCoursesListView;
    @FXML private Label workloadIndexLabel;
    @FXML private Button btnConfirm;

    private ManageWorkLoadClass logicController;

    public void setContext(ManageWorkLoadClass controller) {
        this.logicController = controller;
        workloadIndexLabel.setText(String.format("%.1f", controller.getTotalWorkloadIndex()));

        sortedCoursesListView.getItems().clear();
        for (ManageWorkLoadClass.CourseWorkload cw : controller.getSortedCourses()) {
            sortedCoursesListView.getItems().add(cw.name + "  [Δείκτης: " + cw.workload + "]");
        }
    }

    @FXML
    private void onConfirmAnalysis() {
        boolean saved = logicController.confirmAndSave();
        if (saved) {
            showSuccessWindow();
        }
    }

    private void showSuccessWindow() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/common/success-window-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/common/success-window-view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            com.unipath.ui.common.SuccessScreen successController = loader.getController();
            successController.setSuccessMessage("Ο δείκτης φόρτου εργασίας υπολογίστηκε και αποθηκεύτηκε με επιτυχία στο προφίλ σας!");

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(btnConfirm.getScene().getWindow());
            popupStage.setScene(new Scene(root, 500, 300));
            popupStage.setTitle("UniPath - Επιτυχία");

            // Παγώνει η εκτέλεση μέχρι ο χρήστης να πατήσει OK στο popup
            popupStage.showAndWait();

            // Μόλις πατηθεί, αλλάζουμε τη σκηνή του κεντρικού παραθύρου
            redirectToMainMenu();

        } catch (Exception e) {
            System.err.println("❌ Απέτυχε η εμφάνιση του παραθύρου επιτυχίας:");
            e.printStackTrace();
        }
    }

    private void redirectToMainMenu() {
        try {
            URL mainFxml = getClass().getResource("/fxml/Student/student-main-view.fxml");
            if (mainFxml == null) {
                mainFxml = getClass().getClassLoader().getResource("fxml/Student/student-main-view.fxml");
            }

            Parent mainRoot = FXMLLoader.load(mainFxml);
            Stage mainStage = (Stage) btnConfirm.getScene().getWindow();
            mainStage.setScene(new Scene(mainRoot, 1000, 650));
            mainStage.setTitle("UniPath - Κεντρικό Μενού");
            mainStage.show();
        } catch (Exception e) {
            System.err.println("❌ Απέτυχε η αυτόματη επιστροφή στο μενού: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelAnalysis() {
        redirectToMainMenu();
    }
}