package com.unipath.ui.UC7;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import com.unipath.controller.ManageMentorProfile;
import com.unipath.model.Course;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class NewOfferFormScreen {

    @FXML private ComboBox<String> courseComboBox;
    @FXML private ComboBox<String> helpTypeComboBox;
    @FXML private Button nextButton;
    private ManageMentorProfile controller;
    private final Map<String, String> courseTitleToIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        controller = new ManageMentorProfile();
        List<Course> availableCourses = controller.queryCourses();

        if (availableCourses != null && !availableCourses.isEmpty()) {
            for (Course course : availableCourses) {
                String courseTitle = course.getTitle();
                String courseId = course.getCourseID();
                courseComboBox.getItems().add(courseTitle);
                courseTitleToIdMap.put(courseTitle, courseId);
            }
        } else {
            courseComboBox.setPromptText("-- Δεν υπάρχουν διαθέσιμα μαθήματα --");
        }

        helpTypeComboBox.getItems().addAll(
                "Προσωπικές Σημειώσεις",
                "Φροντιστηριακή Βοήθεια / Επεξήγηση",
                "Υποστήριξη σε Εργαστήριο",
                "Τηλεδιάσκεψη Q&A"
        );
    }

    @FXML
    private void handleNextStep() {
        // Καθαρή ανάκτηση (αφού είναι ComboBox<String>, επιστρέφουν απευθείας String)
        String selectedCourseTitle = courseComboBox.getValue();
        String assistanceType = helpTypeComboBox.getValue();

        if (selectedCourseTitle == null || assistanceType == null) {
            controller.showErrorPopup("Παρακαλώ επιλέξτε Μάθημα και Τύπο Βοήθειας πριν προχωρήσετε.");
            return;
        }

        // Ανάκτηση του ID χρησιμοποιώντας τον τίτλο που είναι πλέον εγγυημένα non-null
        String selectedCourseId = courseTitleToIdMap.get(selectedCourseTitle);

        // Πλοήγηση στη δεύτερη φόρμα
        controller.navigateToOfferSubmission(selectedCourseId, assistanceType);
    }

    /**
     * Καλείται όταν πατηθεί το κουμπί "Ακύρωση"
     */
    @FXML
    private void handleCancel() {
        controller.returnToMainMenu();
    }

    public void display() {
        System.out.println("Εμφάνιση οθόνης: NewOfferFormScreen");
    }
}