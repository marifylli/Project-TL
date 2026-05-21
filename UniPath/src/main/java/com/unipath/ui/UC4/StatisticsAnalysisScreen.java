package com.unipath.ui.UC4;

import com.unipath.controller.ManageStatAnalysis;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Map;

public class StatisticsAnalysisScreen {
    private final ManageStatAnalysis manageStatAnalysis;
    private final Stage stage;

    public StatisticsAnalysisScreen(ManageStatAnalysis manageStatAnalysis) {
        this.manageStatAnalysis = manageStatAnalysis;
        this.stage = new Stage();
    }

    // vima 8: emfanisi statistikin

    public void displayStatistics() {
        stage.setTitle("Ανάλυση Στατιστικών");

        Map<String, Object> stats = manageStatAnalysis.getComputedStatistics();

        // titlos
        Label titleLabel = new Label("Αποτελέσματα Στατιστικής Ανάλυσης");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        // total plans
        int totalPlans = stats.containsKey("totalPlans")
                ? (int) stats.get("totalPlans") : 0;
        Label totalLabel = new Label("Συνολικά Οριστικοποιημένα Πλάνα: " + totalPlans);
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        // ects
        double avgEcts = stats.containsKey("averageECTS")
                ? (double) stats.get("averageECTS") : 0.0;
        Label avgEctsLabel = new Label(
                String.format("Μέσος Όρος ECTS: %.1f", avgEcts));

        // plans per direction
        Label directionTitle = new Label("Κατανομή ανά Κατεύθυνση:");
        directionTitle.setFont(Font.font("System", FontWeight.BOLD, 13));

        VBox directionBox = new VBox(5);
        if (stats.containsKey("plansPerDirection")) {
            @SuppressWarnings("unchecked")
            Map<String, Long> perDirection =
                    (Map<String, Long>) stats.get("plansPerDirection");
            perDirection.forEach((dir, count) -> {
                Label lbl = new Label("  • " + dir + ": " + count + " πλάνα");
                directionBox.getChildren().add(lbl);
            });
        }

        Label yearTitle = new Label("Κατανομή ανά Ακαδημαϊκό Έτος:");
        yearTitle.setFont(Font.font("System", FontWeight.BOLD, 13));

        VBox yearBox = new VBox(5);
        if (stats.containsKey("plansPerYear")) {
            @SuppressWarnings("unchecked")
            Map<String, Long> perYear =
                    (Map<String, Long>) stats.get("plansPerYear");
            perYear.forEach((year, count) -> {
                Label lbl = new Label("  • " + year + ": " + count + " πλάνα");
                yearBox.getChildren().add(lbl);
            });
        }

        // export button
        Button exportButton = new Button("Εξαγωγή Εγγράφου");
        exportButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        exportButton.setMaxWidth(Double.MAX_VALUE);
        exportButton.setOnAction(e -> generateReport());

        Button backButton = new Button("Πίσω στα Φίλτρα");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> goBack());

        HBox buttonBox = new HBox(10, backButton, exportButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // layout
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                totalLabel,
                avgEctsLabel,
                new Separator(),
                directionTitle,
                directionBox,
                new Separator(),
                yearTitle,
                yearBox,
                new Separator(),
                buttonBox
        );

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    // vima 9: o ipallilos pataei eksagogi eggrafou

    private void generateReport() {
        stage.close();
        ReportExportScreen reportScreen =
                new ReportExportScreen(manageStatAnalysis);
        reportScreen.createReport();
    }

    private void goBack() {
        stage.close();
        AnalysisFilterScreen filterScreen =
                new AnalysisFilterScreen(manageStatAnalysis);
        filterScreen.displayFilterForm();
    }

    public Stage getStage() { return stage; }

}
