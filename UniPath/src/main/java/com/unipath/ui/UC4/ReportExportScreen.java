package com.unipath.ui.UC4;

import com.unipath.controller.ManageStatAnalysis;
import com.unipath.model.Report;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;

public class ReportExportScreen {

    @FXML private RadioButton pdfButton;
    @FXML private RadioButton docxButton;
    @FXML private Label filtersLabel;
    @FXML private Label totalPlansLabel;
    @FXML private Label statusLabel;

    private static final int SECRETARY_ID = 1;
    private ManageStatAnalysis manageStatAnalysis;
    private Stage stage;
    private ToggleGroup formatGroup;

    public ReportExportScreen() {}

    public ReportExportScreen(ManageStatAnalysis manageStatAnalysis) {
        this.manageStatAnalysis = manageStatAnalysis;
        this.stage = new Stage();
    }

    @FXML
    public void initialize() {
        formatGroup = new ToggleGroup();
        pdfButton.setToggleGroup(formatGroup);
        docxButton.setToggleGroup(formatGroup);
        pdfButton.setSelected(true);

        if (manageStatAnalysis == null) return;

        filtersLabel.setText(manageStatAnalysis.getCurrentFilters() != null
                ? manageStatAnalysis.getCurrentFilters().toString()
                : "Κανένα φίλτρο");

        int totalPlans = manageStatAnalysis.getComputedStatistics() != null
                && manageStatAnalysis.getComputedStatistics().containsKey("totalPlans")
                ? (int) manageStatAnalysis.getComputedStatistics().get("totalPlans")
                : 0;
        totalPlansLabel.setText("Συνολικά πλάνα: " + totalPlans);
    }

    public void createReport() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/Secretary/report-export-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader().getResource("fxml/Secretary/report-export-view.fxml");
            }
            if (fxmlUrl == null) {
                System.err.println("❌ Δεν βρέθηκε το report-export-view.fxml!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(this);
            Parent root = loader.load();

            stage.setScene(new Scene(root, 1000, 650));
            stage.setTitle("UniPath - Εξαγωγή Επίσημης Αναφοράς");
            stage.show();

        } catch (Exception e) {
            System.err.println("❌ Απέτυχε η φόρτωση της οθόνης εξαγωγής:");
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveReport() {
        String format = pdfButton.isSelected() ? "PDF" : "DOCX";
        Report report = manageStatAnalysis.generateReport(format, SECRETARY_ID);

        if (report != null) {
            statusLabel.setStyle("-fx-text-fill: #38a169; -fx-font-size: 13px;");
            statusLabel.setText("✓ Η αναφορά αποθηκεύτηκε επιτυχώς!");
        } else {
            statusLabel.setStyle("-fx-text-fill: #e53e3e; -fx-font-size: 13px;");
            statusLabel.setText("⚠ Σφάλμα κατά την αποθήκευση.");
        }
    }

    @FXML
    private void onBack() {
        stage.close();
        StatisticsAnalysisScreen statsScreen = new StatisticsAnalysisScreen(manageStatAnalysis);
        statsScreen.displayStatistics();
    }

    public Stage getStage() { return stage; }
}