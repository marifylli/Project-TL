package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Scenario;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import java.util.List;

public class ScenarioSelectionScreen {

    private ManageStudyPlan manageStudyPlan;

    // 💡 ΔΙΟΡΘΩΣΗ: Αλλαγή από ComboBox σε ListView για να ταυτίζεται με το FXML
    @FXML private ListView<Scenario> scenarioListView;
    @FXML private Label errorLabel;

    public ScenarioSelectionScreen() {}

    public ScenarioSelectionScreen(ManageStudyPlan manageStudyPlan) {
        this.manageStudyPlan = manageStudyPlan;
    }

    @FXML
    public void initialize() {
        System.out.println("DEBUG UI: Η initialize() του FXML ολοκληρώθηκε.");
        if (manageStudyPlan != null) {
            displayScenarios();
        }
    }

    public void setManageStudyPlan(ManageStudyPlan manageStudyPlan) {
        this.manageStudyPlan = manageStudyPlan;
        System.out.println("DEBUG UI: Το manageStudyPlan συνδέθηκε με την οθόνη.");
        displayScenarios();
    }

    public void displayScenarios() {
        System.out.println("DEBUG UI: Έναρξη της displayScenarios()...");

        if (scenarioListView == null) {
            System.out.println("❌ ΣΦΑΛΜΑ: Το scenarioListView είναι NULL! Ελέγξτε το fx:id στο FXML.");
            return;
        }
        if (manageStudyPlan == null) {
            System.out.println("❌ ΣΦΑΛΜΑ: Το manageStudyPlan είναι NULL.");
            return;
        }

        scenarioListView.getItems().clear();

        try {
            System.out.println("DEBUG UI: Κλήση της manageStudyPlan.queryGetScenarios()...");
            List<Scenario> scenarios = manageStudyPlan.queryGetScenarios();

            if (scenarios == null || scenarios.isEmpty()) {
                System.out.println("DEBUG UI: Η βάση επέστρεψε 0 σενάρια. Δημιουργία hardcoded σεναρίων ασφαλείας...");
                scenarios = new java.util.ArrayList<>();

                Scenario s1 = new Scenario(); s1.setScenarioId(1); s1.setTitle("Σενάριο 1: Κλασική Κατεύθυνση (Μία Κύρια)"); s1.setDescription("17 μαθήματα"); scenarios.add(s1);
                Scenario s2 = new Scenario(); s2.setScenarioId(2); s2.setTitle("Σενάριο 2: Διπλή Κατεύθυνση"); s2.setDescription("17 μαθήματα"); scenarios.add(s2);
                Scenario s3 = new Scenario(); s3.setScenarioId(3); s3.setTitle("Σενάριο 3: Γενικό Πλάνο"); s3.setDescription("Ελεύθερη επιλογή"); scenarios.add(s3);
            }

            // Γεμίζουμε τη Λίστα
            scenarioListView.getItems().addAll(scenarios);
            scenarioListView.getSelectionModel().selectFirst();
            System.out.println("DEBUG UI: Το ListView ενημερώθηκε επιτυχώς με " + scenarioListView.getItems().size() + " σενάρια!");

        } catch (Exception e) {
            System.err.println("❌ ΣΦΑΛΜΑ κατά τη φόρτωση στο UI:");
            e.printStackTrace();
        }
    }

    public void selectScenario(Scenario scenario) {
        manageStudyPlan.onScenarioSelected(scenario);
    }

    @FXML
    private void handleNextStep() {
        if (scenarioListView == null) return;

        // 💡 Παίρνουμε το επιλεγμένο στοιχείο από τη Λίστα
        Scenario selected = scenarioListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            if (errorLabel != null) {
                errorLabel.setText("Παρακαλώ επιλέξτε ένα σενάριο προτού συνεχίσετε.");
                errorLabel.setVisible(true);
            }
            return;
        }
        selectScenario(selected);
    }
}