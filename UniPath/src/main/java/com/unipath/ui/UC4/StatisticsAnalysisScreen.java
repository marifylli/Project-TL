package com.unipath.ui.UC4;

import com.unipath.controller.ManageStatAnalysis;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;

public class StatisticsAnalysisScreen {

    @FXML private Label totalPlansLabel;
    @FXML private Label avgEctsLabel;
    @FXML private Label perDirectionLabel;
    @FXML private Label perYearLabel;

    private ManageStatAnalysis manageStatAnalysis;
    private Stage stage;

    public StatisticsAnalysisScreen() {}

    public StatisticsAnalysisScreen(ManageStatAnalysis manageStatAnalysis) {
        this.manageStatAnalysis = manageStatAnalysis;
        this.stage = new Stage();
    }

    @FXML
    public void initialize() {
        if (manageStatAnalysis == null) return;

        Map<String, Object> stats = manageStatAnalysis.getComputedStatistics();
        if (stats == null) return;

        int totalPlans = stats.containsKey("totalPlans") ? (int) stats.get("totalPlans") : 0;
        totalPlansLabel.setText(String.valueOf(totalPlans));

        double avgEcts = stats.containsKey("averageECTS") ? (double) stats.get("averageECTS") : 0.0;
        avgEctsLabel.setText(String.format("%.1f", avgEcts));

        if (stats.containsKey("plansPerDirection")) {
            @SuppressWarnings("unchecked")
            Map<String, Long> perDirection = (Map<String, Long>) stats.get("plansPerDirection");
            StringBuilder sb = new StringBuilder();
            perDirection.forEach((dir, count) -> sb.append(dir).append(": ").append(count).append(" πλάνα\n"));
            perDirectionLabel.setText(sb.toString().trim());
        }

        if (stats.containsKey("plansPerYear")) {
            @SuppressWarnings("unchecked")
            Map<String, Long> perYear = (Map<String, Long>) stats.get("plansPerYear");
            StringBuilder sb = new StringBuilder();
            perYear.forEach((year, count) -> sb.append(year).append(": ").append(count).append(" πλάνα\n"));
            perYearLabel.setText(sb.toString().trim());
        }
    }

    public void displayStatistics() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/Secretary/statistics-analysis-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Secretary/statistics-analysis-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println(" Δεν βρέθηκε το statistics-analysis-view.fxml!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(this);
            Parent root = loader.load();

            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Ανάλυση Στατιστικών");
            stage.show();

        } catch (Exception e) {
            System.err.println(" Απέτυχε η φόρτωση της οθόνης στατιστικών:");
            e.printStackTrace();
        }
    }

    @FXML
    private void onExportReport() {
        stage.close();
        ReportExportScreen reportScreen = new ReportExportScreen(manageStatAnalysis);
        reportScreen.createReport();
    }

    @FXML
    private void onBack() {
        stage.close();
        AnalysisFilterScreen filterScreen = new AnalysisFilterScreen(manageStatAnalysis);
        filterScreen.displayFilterForm();
    }

    public Stage getStage() { return stage; }
}