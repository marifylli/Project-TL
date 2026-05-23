package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Course;
import com.unipath.model.Scenario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import java.util.List;


public class CourseSelectionScreen {

    private ManageStudyPlan manageStudyPlan;
    private Scenario scenario;
    @FXML private Label scenarioTitleLabel;
    @FXML private ListView<Course> courseListView;
    @FXML private Label errorLabel;


    public CourseSelectionScreen() {
    }

    public CourseSelectionScreen(ManageStudyPlan manageStudyPlan, Scenario scenario) {
        this.manageStudyPlan = manageStudyPlan;
        this.scenario = scenario;
    }
    public void setSelectionData(ManageStudyPlan manageStudyPlan, Scenario scenario) {
        this.manageStudyPlan = manageStudyPlan;
        this.scenario = scenario;

        if (scenarioTitleLabel != null && scenario != null) {
            scenarioTitleLabel.setText("Επιλεγμένο: " + scenario.toString());
        }

        // Ρύθμιση για πολλαπλή επιλογή (Ctrl + Κλικ) όπως ζητάει το FXML
        if (courseListView != null) {
            courseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        displayCourses();
    }

    public void displayCourses() {
        if (courseListView != null && manageStudyPlan != null) {
            courseListView.getItems().clear();

            // Καλούμε τον κεντρικό controller, ο οποίος θα πάρει τα δεδομένα από το CourseRepository
            List<Course> courses = manageStudyPlan.queryGetCourses();

            if (courses != null) {
                courseListView.getItems().addAll(courses);
            }
        }
    }

    public void selectCourses(List<Course> courses) {

        manageStudyPlan.onCoursesSelected(scenario, courses);
    }

    @FXML
    private void handleNext() {
        List<Course> selectedCourses = courseListView.getSelectionModel().getSelectedItems();
        if (selectedCourses.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Πρέπει να επιλέξετε μαθήματα για το πρόγραμμα!");
                errorLabel.setVisible(true);
            }
            return;
        }
        // Καλεί την αρχική σου μέθοδο selectCourses
        selectCourses(selectedCourses);
    }

    @FXML
    private void handleBack() {
        manageStudyPlan.startCreatePlan();
    }

}
