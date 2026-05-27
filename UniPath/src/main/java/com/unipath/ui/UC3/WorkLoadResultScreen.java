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

    @FXML
    private ListView<String> sortedCoursesListView;
    @FXML
    private Label workloadIndexLabel;
    @FXML
    private Button btnConfirm;

    private ManageWorkLoadClass logicController;

    public void setContext(ManageWorkLoadClass controller) {
        this.logicController = controller;

        double total = controller.getTotalWorkloadIndex();
        int count = controller.getSortedCourses().size();
        double average = count > 0 ? total / count : 0.0;

        String level;
        if (average >= 8.0) level = "Υψηλός Φόρτος";
        else if (average >= 6.5) level = "Μέτριος Φόρτος";
        else level = "Χαμηλός Φόρτος";

        workloadIndexLabel.setText(String.format("%.1f / 10  —  %s", average, level));

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
            if (fxmlUrl == null)
                fxmlUrl = getClass().getClassLoader().getResource("fxml/common/success-window-view.fxml");

            if (fxmlUrl != null) {
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                com.unipath.ui.common.SuccessScreen successController = loader.getController();
                successController.setSuccessMessage("Η προσομοίωση ολοκληρώθηκε και ο δείκτης φόρτου αποθηκεύτηκε με επιτυχία!");

                Stage currentStage = (Stage) btnConfirm.getScene().getWindow();

                Stage successStage = new Stage();
                successStage.setTitle("UniPath - Επιτυχής Ολοκλήρωση");
                successStage.setScene(new Scene(root));
                successStage.initModality(Modality.APPLICATION_MODAL);
                successStage.setOnHiding(e -> currentStage.close());
                successStage.show();
            }
        } catch (Exception e) {
            System.err.println("Απέτυχε η φόρτωση της οθόνης επιτυχίας:");
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelAnalysis() {
        System.out.println("Ακύρωση ανάλυσης. Επιστροφή στο Κεντρικό Μενού.");
        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.close();
    }
}


