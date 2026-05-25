package com.unipath.ui.UC1;

import com.unipath.controller.ManageStudyPlan;
import com.unipath.model.Course;
import com.unipath.model.Scenario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseSelectionScreen {

    private ManageStudyPlan manageStudyPlan;
    private Scenario scenario;
    @FXML private Label scenarioTitleLabel;
    // 1. Αλλαγή σε String
    @FXML private ListView<String> courseListView;
    @FXML private Label errorLabel;

    // 2. Προσθήκη του χάρτη (Map) αμέσως από κάτω
    private final java.util.Map<String, Course> courseTitleMap = new java.util.HashMap<>();


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
            courseTitleMap.clear(); // Καθαρίζουμε τον χάρτη

            List<Course> courses = manageStudyPlan.queryGetCourses();

            if (courses != null && !courses.isEmpty()) {
                for (Course course : courses) {
                    // Φτιάχνουμε το κείμενο που θα φαίνεται στην οθόνη
                    String displayString = course.getTitle() + " (" + course.getECTS() + " ECTS)";

                    courseListView.getItems().add(displayString); // Το βάζουμε στη λίστα
                    courseTitleMap.put(displayString, course);    // Το αντιστοιχίζουμε στον χάρτη
                }
            }
        }
    }

    public void selectCourses(List<Course> courses) {

        manageStudyPlan.onCoursesSelected(scenario, courses);
    }

    @FXML
    private void handleNextStep() {
        // 1. Παίρνουμε τα επιλεγμένα κείμενα (Strings)
        List<String> selectedTitles = courseListView.getSelectionModel().getSelectedItems();

        if (selectedTitles == null || selectedTitles.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("Πρέπει να επιλέξετε μαθήματα για το πρόγραμμα!");
                errorLabel.setVisible(true);
            }
            return;
        }

        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }

        // 2. Μετατροπή των Strings πίσω σε αντικείμενα Course μέσω του χάρτη
        List<Course> selectedCoursesList = new ArrayList<>();
        for (String title : selectedTitles) {
            Course c = courseTitleMap.get(title);
            if (c != null) {
                selectedCoursesList.add(c);
            }
        }

        // 3. Στέλνουμε τη διορθωμένη λίστα μαθημάτων στον Controller
        if (manageStudyPlan != null) {
            manageStudyPlan.onCoursesSelected(this.scenario, selectedCoursesList);
        }
    }

    @FXML
    private void handleBack() {
        manageStudyPlan.startCreatePlan();
    }

}
