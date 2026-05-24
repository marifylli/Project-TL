package com.unipath.ui.UC4;

import com.unipath.controller.ManageStatAnalysis;
import com.unipath.model.FilterCriteria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class AnalysisFilterScreen {

    @FXML private ComboBox<String> academicYearCombo;
    @FXML private ComboBox<String> directionCombo;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextField courseIdField;

    private ManageStatAnalysis manageStatAnalysis;
    private Stage stage;

    public AnalysisFilterScreen() {}

    public AnalysisFilterScreen(ManageStatAnalysis manageStatAnalysis) {
        this.manageStatAnalysis = manageStatAnalysis;
        this.stage = new Stage();
    }

    @FXML
    public void initialize() {
        academicYearCombo.getItems().addAll("Όλα", "2024-2025", "2023-2024", "2022-2023", "2021-2022");
        academicYearCombo.setValue("Όλα");

        directionCombo.getItems().addAll("Όλες", "K1", "K2", "K3", "K4", "K5", "K6");
        directionCombo.setValue("Όλες");

        statusCombo.getItems().addAll("Όλες", "FINALIZED", "ACTIVE", "DRAFT");
        statusCombo.setValue("Όλες");
    }

    public void displayFilterForm() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/Secretary/analysis-filter-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Secretary/analysis-filter-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println(" Δεν βρέθηκε το analysis-filter-view.fxml!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(this);
            Parent root = loader.load();

            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Φίλτρα Ανάλυσης Στατιστικών");
            stage.show();

        } catch (Exception e) {
            System.err.println(" Απέτυχε η φόρτωση της οθόνης φίλτρων:");
            e.printStackTrace();
        }
    }

    public void displayFilterForm(Stage parentStage) {
        stage.initOwner(parentStage);
        displayFilterForm();
    }

    @FXML
    private void onViewStatistics() {
        setFiltersCriteria();
        boolean hasData = manageStatAnalysis.selectViewStatistics();

        if (hasData) {
            stage.close();
            StatisticsAnalysisScreen statsScreen = new StatisticsAnalysisScreen(manageStatAnalysis);
            statsScreen.displayStatistics();
        } else {
            showError("Δεν βρέθηκαν δεδομένα για τα επιλεγμένα φίλτρα.");
        }
    }

    @FXML
    private void onClearFilters() {
        academicYearCombo.setValue("Όλα");
        directionCombo.setValue("Όλες");
        statusCombo.setValue("Όλες");
        courseIdField.clear();
    }

    private void setFiltersCriteria() {
        String year      = academicYearCombo.getValue();
        String direction = directionCombo.getValue();
        String status    = statusCombo.getValue();
        String courseId  = courseIdField.getText().trim();

        FilterCriteria filters = new FilterCriteria(
                year.equals("Όλα")       ? null : year,
                direction.equals("Όλες") ? null : direction,
                courseId.isEmpty()       ? null : courseId,
                status.equals("Όλες")   ? null : status
        );
        manageStatAnalysis.setFiltersCriteria(filters);
    }

    private void showError(String message) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/common/error-window-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/common/error-window-view.fxml");
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            com.unipath.ui.common.ErrorScreen controller = loader.getController();
            controller.setErrorMessage(message);

            Stage errorStage = new Stage();
            errorStage.initModality(Modality.APPLICATION_MODAL);
            errorStage.initOwner(stage);
            errorStage.setScene(new Scene(root));
            errorStage.setTitle("UniPath - Σφάλμα");
            errorStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() { return stage; }
}