package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Scenario;
import com.unipath.model.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.util.List;


public class PlanSummaryScreen {
    private ManageStudyPlan manageStudyPlan;
    private Scenario scenario;
    private List<Course> courses;

    @FXML private Label scenarioLabel;
    @FXML private Label ectsLabel;
    @FXML private ListView<Course> summaryListView;

    public PlanSummaryScreen() {
    }



    public PlanSummaryScreen(ManageStudyPlan manageStudyPlan, Scenario scenario, List<Course> courses) {
        this.manageStudyPlan = manageStudyPlan;
        this.scenario = scenario;
        this.courses = courses;
    }
    public void setSummaryData(ManageStudyPlan manageStudyPlan, Scenario scenario, List<Course> courses) {
        this.manageStudyPlan = manageStudyPlan;
        this.scenario = scenario;
        this.courses = courses;

        displaySummary();
    }


    public void displaySummary() {
        // show summary
        if (scenarioLabel != null && scenario != null) {
            scenarioLabel.setText("Σενάριο: " + scenario.toString());
        }
        if (summaryListView != null && courses != null) {
            summaryListView.getItems().clear();
            summaryListView.getItems().addAll(courses);
        }

        // Υπολογισμός και εμφάνιση συνόλου ECTS
        if (ectsLabel != null && courses != null) {
            int totalEcts = courses.stream().mapToInt(Course::getECTS).sum();
            ectsLabel.setText("Συνολικά ECTS: " + totalEcts + " / 270");
        }
    }

    public void confirmSave() {
        manageStudyPlan.onConfirmPlan(scenario, courses);
    }

    @FXML
    private void handleConfirm() {
        confirmSave();
    }
    @FXML
    private void handleBack() {
        manageStudyPlan.onScenarioSelected(scenario);
    }
}
