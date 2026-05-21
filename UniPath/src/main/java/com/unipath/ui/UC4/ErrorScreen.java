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


public class ErrorScreen {
    private final ManageStatAnalysis manageStatAnalysis;
    private final AnalysisFilterScreen filterScreen;
    private final Stage stage;

    public ErrorScreen(ManageStatAnalysis manageStatAnalysis,
                       AnalysisFilterScreen filterScreen) {
        this.manageStatAnalysis = manageStatAnalysis;
        this.filterScreen       = filterScreen;
        this.stage              = new Stage();
    }

    public void displayError(String message) {stage.setTitle("Σφάλμα Ανάλυσης");

        Label titleLabel = new Label("Δεν βρέθηκαν δεδομένα");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-text-fill: #666;");

        Label hintLabel = new Label(
                "Δοκιμάστε να αλλάξετε τα φίλτρα αναζήτησης.");
        hintLabel.setWrapText(true);
        hintLabel.setStyle("-fx-text-fill: #888; -fx-font-style: italic;");

        VBox messageBox = new VBox(8, titleLabel, messageLabel, hintLabel);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(20));

        Button backButton = new Button("Επιστροφή στα Φίλτρα");
        backButton.setStyle(
                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 13px;");
        backButton.setOnAction(e -> displayFilterForm());

        HBox buttonBox = new HBox(backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        VBox root = new VBox(10, messageBox, new Separator(), buttonBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 380, 260);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void displayFilterForm() {
        stage.close();
        filterScreen.displayFilterForm();
    }

    public Stage getStage() { return stage; }




}




