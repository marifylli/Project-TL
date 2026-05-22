package com.unipath.ui.UC4;

import com.unipath.controller.ManageStatAnalysis;
import com.unipath.model.FilterCriteria;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// othoni filtron analysis: emfanizei tin forma filtron kai tin stelnei ston managestatanalysis otan patithei provoli statistikon

public class AnalysisFilterScreen {
    private final ManageStatAnalysis manageStatAnalysis;
    private final Stage stage;

    // UI fields
    private ComboBox<String> academicYearCombo;
    private ComboBox<String> directionCombo;
    private ComboBox<String> statusCombo;
    private TextField courseIdField;

    public AnalysisFilterScreen(ManageStatAnalysis manageStatAnalysis) {
        this.manageStatAnalysis = manageStatAnalysis;
        this.stage = new Stage();
    }

    // vima 2 emfanisi forma filtron
    public void displayFilterForm() {
        stage.setTitle("Φίλτρα Ανάλυσης Στατιστικών");

        // titlos
        Label titleLabel = new Label("Φίλτρα Ανάλυσης");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        // filter acadamic year
        Label yearLabel = new Label("Ακαδημαϊκό Έτος:");
        academicYearCombo = new ComboBox<>();
        academicYearCombo.getItems().addAll("Όλα", "2024-2025", "2023-2024", "2022-2023", "2021-2022");
        academicYearCombo.setValue("Όλα");
        academicYearCombo.setMaxWidth(Double.MAX_VALUE);

        // kateuthinsi
        Label directionLabel = new Label("Κατεύθυνση:");
        directionCombo = new ComboBox<>();
        directionCombo.getItems().addAll("Όλες", "K1", "K2", "K3", "K4", "K5", "K6");
        directionCombo.setValue("Όλες");
        directionCombo.setMaxWidth(Double.MAX_VALUE);

        // Status
        Label statusLabel = new Label("Κατάσταση Πλάνου:");
        statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Όλες", "FINALIZED", "ACTIVE", "DRAFT");
        statusCombo.setValue("Όλες");
        statusCombo.setMaxWidth(Double.MAX_VALUE);

        // id mathimatos
        Label courseLabel = new Label("Κωδικός Μαθήματος (προαιρετικό):");
        courseIdField = new TextField();
        courseIdField.setPromptText("π.χ. CEID_24Y334");

        // grid
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        grid.add(yearLabel,      0, 0); grid.add(academicYearCombo, 1, 0);
        grid.add(directionLabel, 0, 1); grid.add(directionCombo,    1, 1);
        grid.add(statusLabel,    0, 2); grid.add(statusCombo,       1, 2);
        grid.add(courseLabel,    0, 3); grid.add(courseIdField,     1, 3);

        ColumnConstraints col1 = new ColumnConstraints(180);
        ColumnConstraints col2 = new ColumnConstraints(220);
        grid.getColumnConstraints().addAll(col1, col2);

        // buttons
        Button viewStatsButton = new Button("Προβολή Στατιστικών");
        viewStatsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        viewStatsButton.setMaxWidth(Double.MAX_VALUE);
        viewStatsButton.setOnAction(e -> selectViewStatistics());

        Button clearButton = new Button("Καθαρισμός Φίλτρων");
        clearButton.setMaxWidth(Double.MAX_VALUE);
        clearButton.setOnAction(e -> clearFilters());

        HBox buttonBox = new HBox(10, clearButton, viewStatsButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 20, 20, 20));

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(titleLabel, new Separator(), grid, buttonBox);

        Scene scene = new Scene(root, 460, 320);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // o ypallilos rithmizei ta filtra
    public void setFiltersCriteria() {
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

    // o ypallilos pataei provoli statistikon
    private void selectViewStatistics() {
        setFiltersCriteria();

        boolean hasData = manageStatAnalysis.selectViewStatistics();

        if (hasData) {
            stage.close();
            StatisticsAnalysisScreen statsScreen =
                    new StatisticsAnalysisScreen(manageStatAnalysis);
            statsScreen.displayStatistics();
        } else {
            ErrorScreen errorScreen = new ErrorScreen(manageStatAnalysis, this);
            errorScreen.displayError("Δεν βρέθηκαν δεδομένα για τα επιλεγμένα φίλτρα.");
        }
    }

    private void clearFilters() {
        academicYearCombo.setValue("Όλα");
        directionCombo.setValue("Όλες");
        statusCombo.setValue("Όλες");
        courseIdField.clear();
    }

    public void displayFilterForm(Stage parentStage) {
        stage.initOwner(parentStage);
        displayFilterForm();
    }

    public Stage getStage() { return stage; }

}