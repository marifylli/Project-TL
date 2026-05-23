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

    @FXML
    private Label scenarioTitleLabel;

    @FXML
    private ListView<Course> summaryListView;

    @FXML
    private Label totalEctsLabel;

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

        // 1. Εμφάνιση του τίτλου του ακαδημαϊκού σεναρίου
        if (scenarioTitleLabel != null && scenario != null) {
            scenarioTitleLabel.setText("Σενάριο: " + scenario.getTitle());
        }

        // 2. Γέμισμα του ListView με τα εγκεκριμένα μαθήματα
        if (summaryListView != null && courses != null) {
            summaryListView.getItems().setAll(courses);

            // Προαιρετικά: Μπορείς να βάλεις CellFactory και εδώ αν θες συγκεκριμένο φορμάτ εμφάνισης
            summaryListView.setCellFactory(param -> new javafx.scene.control.ListCell<Course>() {
                @Override
                protected void updateItem(Course item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getCourseID() + " - " + item.getTitle() + " (" + item.getECTS() + " ECTS)");
                    }
                }
            });
        }

        // 3. Υπολογισμός και εμφάνιση συνολικών ECTS
        if (totalEctsLabel != null && courses != null) {
            int totalEcts = courses.stream().mapToInt(Course::getECTS).sum();
            totalEctsLabel.setText("Συνολικά ECTS: " + totalEcts);
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
