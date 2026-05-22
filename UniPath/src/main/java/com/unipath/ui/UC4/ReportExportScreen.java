package com.unipath.ui.UC4;

import com.unipath.controller.ManageStatAnalysis;
import com.unipath.model.Report;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ReportExportScreen {

    private final ManageStatAnalysis manageStatAnalysis;
    private final Stage stage;

    // Hardcoded gia tora, prepei na doume pos tha to kanoyme sto login
    private static final int SECRETARY_ID = 1;

    public ReportExportScreen(ManageStatAnalysis manageStatAnalysis) {
        this.manageStatAnalysis = manageStatAnalysis;
        this.stage = new Stage();
    }

    // vima 10: dimiourgia anaforas

    public void createReport() {
        stage.setTitle("Εξαγωγή Επίσημης Αναφοράς");

        // titlos
        Label titleLabel = new Label("Εξαγωγή Αναφοράς");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        // format
        Label formatLabel = new Label("Επιλογή Μορφής Εγγράφου:");
        formatLabel.setFont(Font.font("System", FontWeight.BOLD, 13));

        ToggleGroup formatGroup = new ToggleGroup();
        RadioButton pdfButton  = new RadioButton("PDF");
        RadioButton docxButton = new RadioButton("DOCX");
        pdfButton.setToggleGroup(formatGroup);
        docxButton.setToggleGroup(formatGroup);
        pdfButton.setSelected(true);

        HBox formatBox = new HBox(20, pdfButton, docxButton);
        formatBox.setAlignment(Pos.CENTER_LEFT);

        // filtra used
        Label filtersTitle = new Label("Φίλτρα που χρησιμοποιήθηκαν:");
        filtersTitle.setFont(Font.font("System", FontWeight.BOLD, 13));

        String filtersText = manageStatAnalysis.getCurrentFilters() != null
                ? manageStatAnalysis.getCurrentFilters().toString()
                : "Κανένα φίλτρο";
        Label filtersLabel = new Label(filtersText);
        filtersLabel.setWrapText(true);

        // statistika
        Label statsTitle = new Label("Σύνοψη Στατιστικών:");
        statsTitle.setFont(Font.font("System", FontWeight.BOLD, 13));

        int totalPlans = manageStatAnalysis.getComputedStatistics() != null
                && manageStatAnalysis.getComputedStatistics().containsKey("totalPlans")
                ? (int) manageStatAnalysis.getComputedStatistics().get("totalPlans")
                : 0;
        Label statsLabel = new Label("Συνολικά πλάνα: " + totalPlans);

        // status
        Label statusLabel = new Label("");
        statusLabel.setWrapText(true);

        // buttons

        Button saveButton = new Button("Αποθήκευση Αναφοράς");
        saveButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        saveButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setOnAction(e -> {
            String format = pdfButton.isSelected() ? "PDF" : "DOCX";
            saveReport(format, statusLabel);
        });

        Button backButton = new Button("Πίσω");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> goBack());

        HBox buttonBox = new HBox(10, backButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);


        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                formatLabel,
                formatBox,
                new Separator(),
                filtersTitle,
                filtersLabel,
                new Separator(),
                statsTitle,
                statsLabel,
                new Separator(),
                statusLabel,
                buttonBox
        );

        Scene scene = new Scene(root, 460, 420);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // vima 11 gia apothikeusi

    private void saveReport(String format, Label statusLabel) {
        Report report = manageStatAnalysis.generateReport(format, SECRETARY_ID);

        if (report != null) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Η αναφορά αποθηκεύτηκε επιτυχώς!");
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Σφάλμα κατά την αποθήκευση.");
        }
    }

    private void goBack() {
        stage.close();
        StatisticsAnalysisScreen statsScreen =
                new StatisticsAnalysisScreen(manageStatAnalysis);
        statsScreen.displayStatistics();
    }

    public Stage getStage() { return stage; }


}
